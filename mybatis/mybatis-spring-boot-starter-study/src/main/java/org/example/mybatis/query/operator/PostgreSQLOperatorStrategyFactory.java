package org.example.mybatis.query.operator;

import static org.example.mybatis.query.operator.SQLOperatorStrategyFactory.registerStrategy;

public class PostgreSQLOperatorStrategyFactory {

  static {
    registerStringOperatorStrategies();
    registerJsonOperatorStrategies();
    registerArrayOperatorStrategies();
    registerRangeOperatorStrategies();
    registerOtherOperatorStrategies();
  }

  private static void registerStringOperatorStrategies() {
    registerStrategy(SQLOperator.ILIKE, (column, index) ->
        String.format("%s ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.NOT_ILIKE, (column, index) ->
        String.format("%s NOT ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')", column, index));

    registerStrategy(SQLOperator.SIMILAR_TO, (column, index) ->
        String.format("%s SIMILAR TO #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.NOT_SIMILAR_TO, (column, index) ->
        String.format("%s NOT SIMILAR TO #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_MATCH, (column, index) ->
        String.format("%s ~ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_MATCH_CASE_INSENSITIVE, (column, index) ->
        String.format("%s ~* #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_NOT_MATCH, (column, index) ->
        String.format("%s !~ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.REGEX_NOT_MATCH_CASE_INSENSITIVE, (column, index) ->
        String.format("%s !~* #{simpleConditions[%d].value}", column, index));
  }

  private static void registerJsonOperatorStrategies() {
    registerStrategy(SQLOperator.JSON_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS, (column, index) ->
        String.format("%s ? #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS_ANY, (column, index) ->
        String.format("%s ?| #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.JSON_EXISTS_ALL, (column, index) ->
        String.format("%s ?& #{simpleConditions[%d].value}", column, index));
  }

  private static void registerArrayOperatorStrategies() {
    registerStrategy(SQLOperator.ARRAY_EQUALS, (column, index) ->
        String.format("%s = #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_NOT_EQUALS, (column, index) ->
        String.format("%s <> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.ARRAY_OVERLAP, (column, index) ->
        String.format("%s && #{simpleConditions[%d].value}", column, index));
  }

  private static void registerRangeOperatorStrategies() {
    registerStrategy(SQLOperator.RANGE_CONTAINS, (column, index) ->
        String.format("%s @> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_CONTAINED_BY, (column, index) ->
        String.format("%s <@ #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_OVERLAP, (column, index) ->
        String.format("%s && #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_LEFT, (column, index) ->
        String.format("%s << #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_RIGHT, (column, index) ->
        String.format("%s >> #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.RANGE_ADJACENT, (column, index) ->
        String.format("%s -|- #{simpleConditions[%d].value}", column, index));
  }
  
  private static void registerOtherOperatorStrategies() {
    // Distinct Operators
    registerStrategy(SQLOperator.IS_DISTINCT_FROM, (column, index) ->
        String.format("%s IS DISTINCT FROM #{simpleConditions[%d].value}", column, index));

    registerStrategy(SQLOperator.IS_NOT_DISTINCT_FROM, (column, index) ->
        String.format("%s IS NOT DISTINCT FROM #{simpleConditions[%d].value}", column, index));
  }
}
