package cn.maiaimei.example.logback;

import static cn.maiaimei.example.constant.Constants.EMPTY_STRING;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.selector.ContextSelector;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.sift.AbstractDiscriminator;
import java.util.Map;
import org.springframework.util.StringUtils;

public class JNDIAndMDCBasedContextDiscriminator extends AbstractDiscriminator<ILoggingEvent> {

  private String key;
  private String mdcKey;
  private String contextDefaultValue;

  public JNDIAndMDCBasedContextDiscriminator() {
  }

  @Override
  public String getDiscriminatingValue(ILoggingEvent event) {
    final String mdcProperty = getMDCProperty(event);
    if (StringUtils.hasText(mdcProperty)) {
      return mdcProperty;
    }
    return getContextName();
  }

  private String getContextName() {
    ContextSelector selector = ContextSelectorStaticBinder.getSingleton().getContextSelector();
    if (selector == null) {
      return this.contextDefaultValue;
    } else {
      LoggerContext lc = selector.getLoggerContext();
      return lc == null ? this.contextDefaultValue : lc.getName();
    }
  }

  private String getMDCProperty(ILoggingEvent event) {
    Map<String, String> mdcMap = event.getMDCPropertyMap();
    if (mdcMap == null) {
      return EMPTY_STRING;
    } else {
      String mdcValue = mdcMap.get(this.mdcKey);
      return mdcValue == null ? EMPTY_STRING : mdcValue;
    }
  }

  @Override
  public String getKey() {
    return this.key;
  }

  public void setKey(String key) {
    this.key = key;
  }

  public String getMdcKey() {
    return mdcKey;
  }

  public void setMdcKey(String mdcKey) {
    this.mdcKey = mdcKey;
  }

  public String getContextDefaultValue() {
    return contextDefaultValue;
  }

  public void setContextDefaultValue(String contextDefaultValue) {
    this.contextDefaultValue = contextDefaultValue;
  }
}
