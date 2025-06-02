package org.example.mybatis.interceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Plugin;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.ConditionGroup;
import org.example.mybatis.query.filter.SimpleCondition;

@Slf4j
@Intercepts({
    @Signature(type = Executor.class, method = "query",
        args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}),
    @Signature(type = Executor.class, method = "update",
        args = {MappedStatement.class, Object.class})
})
public class ConditionInterceptor implements Interceptor {

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    // 添加日志确认拦截器是否执行
    log.info("ConditionInterceptor is executing...");

    Object target = invocation.getTarget();

    if (target instanceof StatementHandler) {
      return handleStatementHandler(invocation);
    } else if (target instanceof Executor) {
      return handleExecutor(invocation);
    }

    return invocation.proceed();
  }

  private Object handleStatementHandler(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

    // 获取 MappedStatement
    MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
    String methodId = mappedStatement.getId();

    log.info("Intercepted method: {}", methodId);

    if (!methodId.endsWith("advancedSelect")) {
      return invocation.proceed();
    }

    // 获取参数
    BoundSql boundSql = statementHandler.getBoundSql();
    Object parameterObject = boundSql.getParameterObject();

    if (parameterObject instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = (Map<String, Object>) parameterObject;

      // 获取 conditions 参数
      @SuppressWarnings("unchecked")
      List<Condition> conditions = (List<Condition>) params.get("conditions");

      if (conditions != null) {
        List<SimpleCondition> simpleConditions = convertToSimpleConditions(conditions);
        params.put("simpleConditions", simpleConditions);
        log.info("Converted {} conditions to {} simple conditions",
            conditions.size(), simpleConditions.size());
      }
    }

    return invocation.proceed();
  }

  private Object handleExecutor(Invocation invocation) throws Throwable {
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    String methodId = ms.getId();

    log.info("Intercepted executor method: {}", methodId);

    if (!methodId.endsWith("advancedSelect")) {
      return invocation.proceed();
    }

    Object parameter = invocation.getArgs()[1];
    if (parameter instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = (Map<String, Object>) parameter;

      @SuppressWarnings("unchecked")
      List<Condition> conditions = (List<Condition>) params.get("conditions");

      if (conditions != null) {
        List<SimpleCondition> simpleConditions = convertToSimpleConditions(conditions);
        params.put("simpleConditions", simpleConditions);
        log.info("Converted {} conditions to {} simple conditions",
            conditions.size(), simpleConditions.size());
      }
    }

    return invocation.proceed();
  }

  private List<SimpleCondition> convertToSimpleConditions(List<Condition> conditions) {
    List<SimpleCondition> simpleConditions = new ArrayList<>();

    for (Condition condition : conditions) {
      if (condition instanceof SimpleCondition) {
        simpleConditions.add((SimpleCondition) condition);
      } else if (condition instanceof ConditionGroup) {
        ConditionGroup group = (ConditionGroup) condition;
        simpleConditions.addAll(convertToSimpleConditions(group.getConditions()));
      }
    }

    return simpleConditions;
  }

  @Override
  public Object plugin(Object target) {
    return Plugin.wrap(target, this);
  }

  @Override
  public void setProperties(Properties properties) {
  }
}
