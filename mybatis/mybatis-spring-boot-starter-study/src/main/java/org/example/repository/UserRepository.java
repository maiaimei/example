package org.example.repository;

import java.math.BigDecimal;
import java.util.List;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.example.domain.User;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface UserRepository {

  // 查询所有用户
  @Select("SELECT * FROM sys_user")
  List<User> findAll();

  // 根据ID查询用户
  @Select("SELECT * FROM sys_user WHERE user_id = #{id}")
  User findById(@Param("id") BigDecimal id);

  // 插入新用户
  @Insert("INSERT INTO sys_user(user_id, nickname, username, password, gmt_create, gmt_modified) VALUES(#{userId}, #{nickname}, "
      + "#{username}, #{password}, #{gmtCreate}, #{gmtModified})")
  void insert(User user);

  // 更新用户信息
  @Update("UPDATE sys_user SET gmt_modified = #{user.gmtModified} WHERE user_id = #{id}")
  void update(User user);

  // 删除用户
  @Delete("DELETE FROM sys_user WHERE user_id = #{id}")
  void deleteById(@Param("id") BigDecimal id);

  // 批量插入用户
  @Insert({"<script>",
      "INSERT INTO sys_user(user_id, nickname, username, password, gmt_create, gmt_modified) VALUES",
      "<foreach collection='users' item='user' separator=','>",
      "(#{userId}, #{nickname}, #{username}, #{password}, #{gmtCreate}, #{gmtModified})",
      "</foreach>",
      "</script>"})
  void batchInsert(@Param("users") List<User> users);

  // 方式1：使用foreach循环的批量更新
  @Update({"<script>",
      "<foreach collection='users' item='user' separator=';'>",
      "UPDATE sys_user",
      "SET gmt_modified = #{user.gmtModified}",
      "WHERE user_id = #{user.userId}",
      "</foreach>",
      "</script>"})
  void batchUpdate(@Param("users") List<User> users);

  // 方式2：使用case when的批量更新
  @Update({"<script>",
      "UPDATE sys_user",
      "<set>",
      "    <if test='updatePassword'>",
      "        password = CASE user_id",
      "        <foreach collection='users' item='user'>",
      "            WHEN #{user.userId} THEN #{user.password}",
      "        </foreach>",
      "        END,",
      "    </if>",
      "    nickname = CASE user_id",
      "    <foreach collection='users' item='user'>",
      "        WHEN #{user.userId} THEN #{user.nickname}",
      "    </foreach>",
      "    END,",
      "    username = CASE user_id",
      "    <foreach collection='users' item='user'>",
      "        WHEN #{user.userId} THEN #{user.username}",
      "    </foreach>",
      "    END,",
      "    gmt_modified = CASE user_id",
      "    <foreach collection='users' item='user'>",
      "        WHEN #{user.userId} THEN #{user.gmtModified}",
      "    </foreach>",
      "    END",
      "</set>",
      "WHERE user_id IN",
      "<foreach collection='users' item='user' open='(' separator=',' close=')'>",
      "    #{user.userId}",
      "</foreach>",
      "</script>"})
  void batchUpdateByCaseWhen(@Param("users") List<User> users, @Param("updatePassword") boolean updatePassword);
}
