package org.example.mybatis.query.filter;

public class QueryConditionBuilderTest {

//  @Test
//  void testVariousOperators() {
//    final DefaultFilterable defaultFilterable = DefaultFilterable.newInstance();
//    defaultFilterable
//        .and(new SimpleCondition("status", SQLOperator.EQ, 1))
//        .and(new SimpleCondition("age", SQLOperator.BETWEEN, 18, 30))
//        .or(new SimpleCondition("username", SQLOperator.LIKE, "john"),
//            new SimpleCondition("email", SQLOperator.LIKE, "test"))
//        .and(new SimpleCondition("role", SQLOperator.IN, Arrays.asList(1, 2, 3)))
//        .and(new SimpleCondition("deleted_at", SQLOperator.IS_NULL, null));
//    String sql = SQLBuilder.builder()
//        .selectAllColumns("users")
//        .whereByConditions(defaultFilterable.getConditions())
//        .build();
//    System.out.println(sql);
//  }
}
