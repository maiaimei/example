package cn.maiaimei.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "custom-filter")
public class CustomFilterProperties {

  private List<String> excludePaths = new ArrayList<>();
}
