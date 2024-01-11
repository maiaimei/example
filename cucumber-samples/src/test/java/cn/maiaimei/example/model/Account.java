package cn.maiaimei.example.model;

import lombok.Data;

@Data
public class Account {

  /**
   * 帐号
   */
  private String username;

  /**
   * 角色
   */
  private String role;

  /**
   * 项目
   */
  private String project;

}
