package org.example.mybatis.query.filter;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import lombok.extern.slf4j.Slf4j;
import org.example.mybatis.SQLBuilder;
import org.example.mybatis.query.operator.SQLOperator;
import org.junit.jupiter.api.Test;

@Slf4j
public class QueryConditionBuilderTest {

  @Test
  void testVariousOperators() {
    AtomicInteger index = new AtomicInteger(0);
    final List<Condition> conditions = QueryConditionBuilder.create()
        .and(new SimpleCondition("status", SQLOperator.EQ, 1))
        .and(new SimpleCondition("age", SQLOperator.BETWEEN, 18, 30))
        .or(new SimpleCondition("username", SQLOperator.LIKE, "john"),
            new SimpleCondition("email", SQLOperator.LIKE, "test"))
        .and(new SimpleCondition("role", SQLOperator.IN, Arrays.asList(1, 2, 3)))
        .and(new SimpleCondition("deleted_at", SQLOperator.IS_NULL, null)).build();
    String sql = SQLBuilder.builder()
        .selectAllColumns("users")
        .whereByConditions(conditions, index)
        .build();
    log.info("\n{}", sql);
  }
}
