package org.example.autoconfigure;

import lombok.Data;

@Data
public class DataSourceProperties {

  private String url;
  private String username;
  private String password;
  private String driverClassName;

  // 连接池配置
  private Pool pool = new Pool();

  @Data
  public static class Pool {

    private int initialSize = 5;
    private int minIdle = 5;
    private int maxActive = 20;
    private long maxWait = 60000;
    private long timeBetweenEvictionRunsMillis = 60000;
    private long minEvictableIdleTimeMillis = 300000;
  }
}
