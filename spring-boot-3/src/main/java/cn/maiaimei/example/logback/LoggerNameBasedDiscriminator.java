package cn.maiaimei.example.logback;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.AbstractDiscriminator;

public class LoggerNameBasedDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

  private static final String KEY = "loggerName";
  private String defaultValue;

  public LoggerNameBasedDiscriminator() {
  }

  public String getDefaultValue() {
    return defaultValue;
  }

  public void setDefaultValue(String defaultValue) {
    this.defaultValue = defaultValue;
  }

  @Override
  public String getDiscriminatingValue(ILoggingEvent event) {
    String loggerName = event.getLoggerName();

    if (loggerName == null) {
      return defaultValue;
    }

    return loggerName;
  }

  @Override
  public String getKey() {
    return KEY;
  }

  public void setKey() {
    throw new UnsupportedOperationException("Key cannot be set. Using fixed key " + KEY);
  }
}
