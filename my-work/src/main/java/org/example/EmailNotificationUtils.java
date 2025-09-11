package org.example;

import static org.example.EmailNotificationConstants.*;

public final class EmailNotificationUtils {

  public static String getTemplateName(boolean withoutLinks) {
    return withoutLinks ? NO_LINKS_IN_EMAIL_TEMPLATE : DEFAULT_EMAIL_TEMPLATE;
  }

  // 可以添加一个工具方法来获取对应的模板名称
  public static String getTemplateNameByType(EmailNotificationRecipientType recipientType, boolean withoutLinks) {
    return switch (recipientType) {
      case OPERATOR -> withoutLinks ? OPERATOR_NO_LINKS_EMAIL_TEMPLATE : OPERATOR_EMAIL_TEMPLATE;
      case OWN_PARTY -> withoutLinks ? OWN_PARTY_NO_LINKS_EMAIL_TEMPLATE : OWN_PARTY_EMAIL_TEMPLATE;
      case COUNTER_PARTY -> withoutLinks ? COUNTER_PARTY_NO_LINKS_EMAIL_TEMPLATE : COUNTER_PARTY_EMAIL_TEMPLATE;
      case ALL_PARTY -> withoutLinks ? ALL_PARTY_NO_LINKS_EMAIL_TEMPLATE : ALL_PARTY_EMAIL_TEMPLATE;
    };
  }
}
