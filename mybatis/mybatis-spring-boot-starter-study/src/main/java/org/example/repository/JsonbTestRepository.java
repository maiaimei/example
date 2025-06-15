package org.example.repository;

import java.math.BigDecimal;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;
import org.example.model.domain.JsonbTest;
import org.example.mybatis.repository.BaseRepository;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface JsonbTestRepository extends BaseRepository<JsonbTest> {

  // 更新顶层key
  @Update("""
      UPDATE "JSONB_TEST" SET\s
      "STRING_DATA" = jsonb_set(
          "STRING_DATA",
          '{name}',
          '"${name}"'::jsonb
      ),
      "PERSON_DATA" = jsonb_set(
          "PERSON_DATA",
          '{name}',
          '"${name}"'::jsonb
      )
      WHERE "ID" = #{id};""")
  void updateName(@Param("id") BigDecimal id, @Param("name") String name);

  // 更新嵌套对象的key
  @Update("""
      UPDATE "JSONB_TEST" SET\s
      "STRING_DATA" = jsonb_set(
          "STRING_DATA",
          '{contact,address}',
          '"${contactAddress}"'::jsonb
      ),
      "PERSON_DATA" = jsonb_set(
          "PERSON_DATA",
          '{contact,address}',
          '"${contactAddress}"'::jsonb
      )
      WHERE "ID" = #{id};""")
  void updateContactAddress(@Param("id") BigDecimal id, @Param("contactAddress") String contactAddress);

  // 向数组添加元素
  @Update("""
      UPDATE "JSONB_TEST" SET\s
      "STRING_DATA" = jsonb_set(
          "STRING_DATA",
          '{tags}',
          ("STRING_DATA"->'tags') || '"${tag}"'::jsonb
      ),
      "PERSON_DATA" = jsonb_set(
          "PERSON_DATA",
          '{tags}',
          ("PERSON_DATA"->'tags') || '"${tag}"'::jsonb
      )
      WHERE "ID" = #{id};""")
  void addTag(@Param("id") BigDecimal id, @Param("tag") String tag);
}
