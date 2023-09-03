package cn.maiaimei.example.config;

public class TestDBConfig {

  public static final String driverClassName = "com.mysql.cj.jdbc.Driver";
  public static final String url =
      "jdbc:mysql://192.168.1.12/testdb?useSSL=false&allowPublicKeyRetrieval=true"
          + "&serverTimezone=GMT%2B8";
  public static final String username = "root";
  public static final String password = "123456";
}
