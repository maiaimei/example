package org.example.mybatis.query.operator;

public final class SQLOperatorFormat {

  public static final String COMPARISON_OPERATOR_FORMAT = """
      ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} #{simpleConditions[${index}].value}""";
  public static final String LIKE_FORMAT = """
      ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} CONCAT('%%', #{simpleConditions[${index}].value}, '%%')""";
  public static final String STARTS_WITH_FORMAT = """
      ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} CONCAT(#{simpleConditions[${index}].value}, '%%')""";
  public static final String ENDS_WITH_FORMAT = """
      ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} CONCAT('%%', #{simpleConditions[${index}].value})""";
  public static final String BETWEEN_FORMAT = """
      ${simpleConditions[${index}].column} BETWEEN #{simpleConditions[${index}].value.startValue} AND #{simpleConditions[${index}].value.endValue}""";
  public static final String IN_FORMAT = """
      <choose>
          <when test='simpleConditions[${index}].value != null and simpleConditions[${index}].value.size() > simpleConditions[${index}].parameters.maxSize'>
              <trim prefix='(' prefixOverrides='OR' suffix=')'>
                  <foreach collection='simpleConditions[${index}].value' item='item' open='' close='' separator='' index='i'>
                      <if test='i %% simpleConditions[${index}].parameters.maxSize == 0'>
                          <choose>
                              <when test='i == 0'>${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} </when>
                              <otherwise> OR ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} </otherwise>
                          </choose>
                      </if>
                      <if test='i %% simpleConditions[${index}].parameters.maxSize == 0'>(</if>
                      #{item}
                      <if test='i %% simpleConditions[${index}].parameters.maxSize == simpleConditions[${index}].parameters.maxSize - 1 or i == simpleConditions[${index}].value.size() - 1'>)</if>
                      <if test='i %% simpleConditions[${index}].parameters.maxSize != simpleConditions[${index}].parameters.maxSize - 1 and i != simpleConditions[${index}].value.size() - 1'>,</if>
                  </foreach>
              </trim>
          </when>
          <otherwise>
              ${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse} <foreach collection='simpleConditions[${index}].value' item='item' open='(' separator=',' close=')'>#{item}</foreach>
          </otherwise>
      </choose>""";
  public static final String NULL_FORMAT = "${simpleConditions[${index}].column} ${simpleConditions[${index}].operatorToUse}";

  // JSONB Text Operators (忽略大小写)
  public static final String JSONB_TEXT_EQ_FORMAT =
      """
          LOWER(${simpleConditions[${index}].column}->>'${simpleConditions[${index}].parameters.jsonPath}') = LOWER(#{simpleConditions[${index}].value})""";
  public static final String JSONB_TEXT_LIKE_FORMAT =
      """
          LOWER(${simpleConditions[${index}].column}->>'${simpleConditions[${index}].parameters.jsonPath}') LIKE LOWER(CONCAT('%%', #{simpleConditions[${index}].value}, '%%'))""";
  public static final String JSONB_TEXT_NOT_LIKE_FORMAT =
      """
          LOWER(${simpleConditions[${index}].column}->>'${simpleConditions[${index}].parameters.jsonPath}') NOT LIKE LOWER(CONCAT('%%', #{simpleConditions[${index}].value}, '%%'))""";

  // JSONB Array Operators
  public static final String JSONB_ARRAY_CONTAINS_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements_text(${simpleConditions[${index}].column}->'${simpleConditions[${index}].parameters.jsonPath}') elem\s
          WHERE LOWER(elem) = LOWER(#{simpleConditions[${index}].value})
      )""";
  public static final String JSONB_ARRAY_LIKE_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements_text(${simpleConditions[${index}].column}->'${simpleConditions[${index}].parameters.jsonPath}') elem\s
          WHERE LOWER(elem) LIKE LOWER(CONCAT('%%', #{simpleConditions[${index}].value}, '%%'))
      )""";

  // JSONB Object Array Operators
  public static final String JSONB_OBJECT_ARRAY_EQ_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements(${simpleConditions[${index}].column}->'${simpleConditions[${index}].parameters.jsonPath}') obj\s
          WHERE LOWER(obj->>'${simpleConditions[${index}].parameters.nestedField}') = LOWER(#{simpleConditions[${index}].value})
      )""";

  public static final String JSONB_OBJECT_ARRAY_LIKE_FORMAT = """
      EXISTS (
          SELECT 1 FROM jsonb_array_elements(${simpleConditions[${index}].column}->'${simpleConditions[${index}].parameters.jsonPath}') obj\s
          WHERE LOWER(obj->>'${simpleConditions[${index}].parameters.nestedField}') LIKE LOWER(CONCAT('%%', #{simpleConditions[${index}].value}, '%%'))
      )""";

  public static final String ARRAY_CONTAINS_FORMAT = """
      ${simpleConditions[${index}].column} @> <foreach collection='simpleConditions[${index}].value' item='item' open='ARRAY[' separator=',' close=']'>#{item}</foreach>""";
}
