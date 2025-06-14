package org.example.mybatis.query.operator;

public final class SQLOperatorFormat {

  // ============================== Standard SQL Operator Formats ==============================
  public static final String EQ_FORMAT = "%s = #{simpleConditions[%d].value}";
  public static final String NE_FORMAT = "%s != #{simpleConditions[%d].value}";
  public static final String GT_FORMAT = "%s > #{simpleConditions[%d].value}";
  public static final String GE_FORMAT = "%s >= #{simpleConditions[%d].value}";
  public static final String LT_FORMAT = "%s < #{simpleConditions[%d].value}";
  public static final String LE_FORMAT = "%s <= #{simpleConditions[%d].value}";
  public static final String LIKE_FORMAT = "%s LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')";
  public static final String STARTS_WITH_FORMAT = "%s LIKE CONCAT(#{simpleConditions[%d].value}, '%%')";
  public static final String ENDS_WITH_FORMAT = "%s LIKE CONCAT('%%', #{simpleConditions[%d].value})";
  public static final String NOT_LIKE_FORMAT = "%s NOT LIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')";
  public static final String BETWEEN_FORMAT = "%s BETWEEN #{simpleConditions[%d].value} AND #{simpleConditions[%d].secondValue}";
  public static final String IN_FORMAT = """
      <choose>
          <when test='simpleConditions[%d].value != null and simpleConditions[%d].value.size() > %d'>
              <trim prefix='(' prefixOverrides='OR' suffix=')'>
                  <foreach collection='simpleConditions[%d].value' item='item' open='' close='' separator='' index='i'>
                      <if test='i %% %d == 0'>
                          <choose>
                              <when test='i == 0'>%s %s </when>
                              <otherwise> OR %s %s </otherwise>
                          </choose>
                      </if>
                      <if test='i %% %d == 0'>(</if>
                      #{item}
                      <if test='i %% %d == %d or i == simpleConditions[%d].value.size() - 1'>)</if>
                      <if test='i %% %d != %d and i != simpleConditions[%d].value.size() - 1'>,</if>
                  </foreach>
              </trim>
          </when>
          <otherwise>
              %s %s <foreach collection='simpleConditions[%d].value' item='item' open='(' separator=',' close=')'>#{item}</foreach>
          </otherwise>
      </choose>""";
  public static final String IS_NULL_FORMAT = "%s IS NULL";
  public static final String IS_NOT_NULL_FORMAT = "%s IS NOT NULL";

  // ============================== PostgreSQL Specific Operator Formats ==============================
  public static final String LIKE_CASE_INSENSITIVE_FORMAT = "%s ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')";
  public static final String NOT_LIKE_CASE_INSENSITIVE_FORMAT = "%s NOT ILIKE CONCAT('%%', #{simpleConditions[%d].value}, '%%')";

  // JSONB Text Operators (忽略大小写)
  public static final String JSONB_TEXT_EQ_FORMAT =
      """
          LOWER(%s->>'#{jsonPath}') = LOWER(#{simpleConditions[%d].value})""";
  public static final String JSONB_TEXT_LIKE_FORMAT =
      "LOWER(%s->>'#{simpleConditions[%d].map.jsonPath}') LIKE LOWER(CONCAT('%%', #{simpleConditions[%d].value}, "
          + "'%%'))";
  public static final String JSONB_TEXT_NOT_LIKE_FORMAT =
      "LOWER(%s->>'#{simpleConditions[%d].map.jsonPath}') NOT LIKE LOWER(CONCAT('%%', #{simpleConditions[%d]"
          + ".value}, '%%'))";

  // JSONB Path Operators
  public static final String JSONB_PATH_EXISTS_FORMAT = "%s ?? #{simpleConditions[%d].value}";
  public static final String JSONB_PATH_EXISTS_ANY_FORMAT = "%s ?| #{simpleConditions[%d].value}";
  public static final String JSONB_PATH_EXISTS_ALL_FORMAT = "%s ?& #{simpleConditions[%d].value}";

  // JSONB Array Operators
  public static final String JSONB_ARRAY_CONTAINS_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements_text(%s->'#{simpleConditions[%d].map.jsonPath}') elem\s
          WHERE LOWER(elem) = LOWER(#{simpleConditions[%d].value})
      )""";

  public static final String JSONB_ARRAY_LIKE_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements_text(%s->'#{simpleConditions[%d].map.jsonPath}') elem\s
          WHERE LOWER(elem) LIKE LOWER(CONCAT('%%', #{simpleConditions[%d].value}, '%%'))
      )""";

  // JSONB Object Array Operators
  public static final String JSONB_OBJECT_ARRAY_EQ_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements(%s->'#{simpleConditions[%d].map.jsonPath}') obj\s
          WHERE LOWER(obj->>'#{simpleConditions[%d].map.nestedField}') = LOWER(#{simpleConditions[%d].value})
      )""";

  public static final String JSONB_OBJECT_ARRAY_LIKE_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements(%s->'#{simpleConditions[%d].map.jsonPath}') obj\s
          WHERE LOWER(obj->>'#{simpleConditions[%d].map.nestedField}') LIKE LOWER(CONCAT('%%', #{simpleConditions[%d].value}, '%%'))
      )""";

  public static final String SIMILAR_TO_FORMAT = "%s SIMILAR TO #{simpleConditions[%d].value}";
  public static final String NOT_SIMILAR_TO_FORMAT = "%s NOT SIMILAR TO #{simpleConditions[%d].value}";
  public static final String REGEX_MATCH_FORMAT = "%s ~ #{simpleConditions[%d].value}";
  public static final String REGEX_MATCH_CASE_INSENSITIVE_FORMAT = "%s ~* #{simpleConditions[%d].value}";
  public static final String REGEX_NOT_MATCH_FORMAT = "%s !~ #{simpleConditions[%d].value}";
  public static final String REGEX_NOT_MATCH_CASE_INSENSITIVE_FORMAT = "%s !~* #{simpleConditions[%d].value}";
  public static final String RANGE_CONTAINS_FORMAT = "%s @> #{simpleConditions[%d].value}";
  public static final String RANGE_CONTAINED_BY_FORMAT = "%s <@ #{simpleConditions[%d].value}";
  public static final String RANGE_OVERLAP_FORMAT = "%s && #{simpleConditions[%d].value}";
  public static final String RANGE_LEFT_FORMAT = "%s << #{simpleConditions[%d].value}";
  public static final String RANGE_RIGHT_FORMAT = "%s >> #{simpleConditions[%d].value}";
  public static final String RANGE_ADJACENT_FORMAT = "%s -|- #{simpleConditions[%d].value}";
  public static final String JSON_CONTAINS_FORMAT = "%s @> #{simpleConditions[%d].value}";
  public static final String JSON_CONTAINED_BY_FORMAT = "%s <@ #{simpleConditions[%d].value}";
  public static final String ARRAY_EQUALS_FORMAT = "%s = #{simpleConditions[%d].value}";
  public static final String ARRAY_NOT_EQUALS_FORMAT = "%s <> #{simpleConditions[%d].value}";
  public static final String ARRAY_CONTAINS_FORMAT = "%s @> #{simpleConditions[%d].value}";
  public static final String ARRAY_CONTAINED_BY_FORMAT = "%s <@ #{simpleConditions[%d].value}";
  public static final String ARRAY_OVERLAP_FORMAT = "%s && #{simpleConditions[%d].value}";
  public static final String IS_DISTINCT_FROM_FORMAT = "%s IS DISTINCT FROM #{simpleConditions[%d].value}";
  public static final String IS_NOT_DISTINCT_FROM_FORMAT = "%s IS NOT DISTINCT FROM #{simpleConditions[%d].value}";
}
