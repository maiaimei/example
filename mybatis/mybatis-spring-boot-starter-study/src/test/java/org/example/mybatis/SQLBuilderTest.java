package org.example.mybatis;

import static org.example.mybatis.query.filter.SimpleConditionFactory.like;

import java.util.Arrays;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.mybatis.query.operator.SQLOperator;
import org.junit.jupiter.api.Test;

@Slf4j
public class SQLBuilderTest {

  @Test
  void testVariousOperators() {
    final List<Condition> conditions = QueryConditionBuilder.create()
        .or(
            like("username", "john"),
            like("email", "test")
        )
        .andEquals("status", 1)
        .andWhere("age", SQLOperator.BETWEEN, 18, 30)
        .andWhere("role", SQLOperator.IN, Arrays.asList(1, 2, 3))
        .andWhere("deleted_at", SQLOperator.IS_NULL, null).build();
    String sql = SQLBuilder.buildSelectQueryWithSort("users", null, conditions, null);
    log.info("\n{}", sql);
  }
}
