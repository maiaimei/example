package org.example.dataSource.autoconfigure;

import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;


@Data
@ConfigurationProperties(prefix = "spring.datasources")
public class MultipleDataSourceProperties {

  private boolean enabled;
  private List<org.example.dataSource.autoconfigure.DataSourceProperties> shard;
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
