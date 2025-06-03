package org.example.mybatis.interceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
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

  // 定义需要拦截的方法后缀
  private static final List<String> INTERCEPTED_METHOD_SUFFIXES = Arrays.asList(
      "advancedSelect",
      "advancedCount",
      "advancedDelete"
  );

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

  private boolean skipIntercept(String methodId) {
    return INTERCEPTED_METHOD_SUFFIXES.stream().noneMatch(methodId::endsWith);
  }

  private Object handleStatementHandler(Invocation invocation) throws Throwable {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

    MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
    String methodId = mappedStatement.getId();

    log.info("Intercepted method: {}", methodId);

    if (skipIntercept(methodId)) {
      return invocation.proceed();
    }

    return processConditions(invocation, statementHandler.getBoundSql().getParameterObject());
  }

  private Object handleExecutor(Invocation invocation) throws Throwable {
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    String methodId = ms.getId();

    log.info("Intercepted executor method: {}", methodId);

    if (skipIntercept(methodId)) {
      return invocation.proceed();
    }

    return processConditions(invocation, invocation.getArgs()[1]);
  }

  private Object processConditions(Invocation invocation, Object parameterObject) throws Throwable {
    if (parameterObject instanceof Map) {
      @SuppressWarnings("unchecked")
      Map<String, Object> params = (Map<String, Object>) parameterObject;

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
      } else if (condition instanceof ConditionGroup group) {
        simpleConditions.addAll(convertToSimpleConditions(group.getConditions()));
      }
    }

    return simpleConditions;
  }
}
