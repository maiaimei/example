package cn.maiaimei.example.model;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import lombok.Data;

@Data
public class DBProperties {

  private String driverClassName;
  private String url;
  private String username;
  private String password;

  public static DBProperties newInstance() {
    DBProperties dbProperties = new DBProperties();
    try (InputStream is = DBProperties.class.getResourceAsStream("/druid.properties")) {
      Properties properties = new Properties();
      properties.load(is);
      dbProperties.setDriverClassName(properties.getProperty("driverClassName"));
      dbProperties.setUrl(properties.getProperty("url"));
      dbProperties.setUsername(properties.getProperty("username"));
      dbProperties.setPassword(properties.getProperty("password"));
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
    return dbProperties;
  }
}
