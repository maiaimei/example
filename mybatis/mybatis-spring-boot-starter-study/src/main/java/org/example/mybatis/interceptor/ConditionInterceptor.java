package org.example.mybatis.interceptor;

import java.util.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.executor.statement.StatementHandler;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.*;
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

  private static final List<String> INTERCEPTED_METHOD_SUFFIXES = Arrays.asList(
      "advancedSelect",
      "advancedCount",
      "advancedDelete"
  );

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    log.info("ConditionInterceptor is executing...");
    Object target = invocation.getTarget();

    if (target instanceof StatementHandler) {
      handleStatementHandler(invocation);
    } else if (target instanceof Executor) {
      handleExecutor(invocation);
    }

    return invocation.proceed();
  }

  private void handleStatementHandler(Invocation invocation) {
    StatementHandler statementHandler = (StatementHandler) invocation.getTarget();
    MetaObject metaObject = SystemMetaObject.forObject(statementHandler);

    MappedStatement mappedStatement = (MappedStatement) metaObject.getValue("delegate.mappedStatement");
    String methodId = mappedStatement.getId();

    log.info("Intercepted method: {}", methodId);

    if (shouldIntercept(methodId)) {
      processConditions(statementHandler.getBoundSql().getParameterObject());
    }
  }

  private void handleExecutor(Invocation invocation) {
    MappedStatement ms = (MappedStatement) invocation.getArgs()[0];
    String methodId = ms.getId();

    log.info("Intercepted executor method: {}", methodId);

    if (shouldIntercept(methodId)) {
      processConditions(invocation.getArgs()[1]);
    }
  }

  private boolean shouldIntercept(String methodId) {
    return INTERCEPTED_METHOD_SUFFIXES.stream().anyMatch(methodId::endsWith);
  }

  private void processConditions(Object parameterObject) {
    if (!(parameterObject instanceof Map)) {
      log.debug("Parameter object is not a Map, skipping condition processing");
      return;
    }

    @SuppressWarnings("unchecked")
    Map<String, Object> params = (Map<String, Object>) parameterObject;

    @SuppressWarnings("unchecked")
    List<Condition> conditions = (List<Condition>) params.get("conditions");

    if (conditions == null || conditions.isEmpty()) {
      log.debug("No conditions found to process");
      return;
    }

    List<SimpleCondition> simpleConditions = convertToSimpleConditions(conditions);
    params.put("simpleConditions", simpleConditions);

    log.info("Successfully processed conditions: converted {} conditions to {} simple conditions",
        conditions.size(), simpleConditions.size());
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
