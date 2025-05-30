package org.example.datasource.autoconfigure;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@EqualsAndHashCode(callSuper = true)
@ConfigurationProperties(prefix = "spring.datasource")
public class SimpleDataSourceProperties extends org.example.datasource.autoconfigure.DataSourceProperties {

  private boolean enabled;
  private Hikari hikari = new Hikari();
  private Druid druid = new Druid();

  @Data
  public static class Hikari {

    private boolean enabled;
  }

  @Data
  public static class Druid {

    private boolean enabled;
  }
}

