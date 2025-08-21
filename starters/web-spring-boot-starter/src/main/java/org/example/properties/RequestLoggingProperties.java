package org.example.properties;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "logging.request")
public class RequestLoggingProperties {

  /**
   * 不需要记录日志的路径patterns
   */
  private List<String> excludePaths = new ArrayList<>();

  /**
   * 慢请求阈值(毫秒)
   */
  private long slowRequestThresholdMs = 5000;

  private String useCustomFilterExcludePaths = "Y";
}

