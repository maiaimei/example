package org.example;

import static org.example.EmailNotificationConstants.*;

public final class EmailNotificationUtils {

  public String getDefaultTemplate(EmailNotificationRecipientType recipientType, boolean withoutLinks) {
    if (EmailNotificationRecipientType.OPERATOR.equals(recipientType)) {
      return withoutLinks ? OPERATOR_EMAIL_TEMPLATE_WITHOUT_LINKS : OPERATOR_EMAIL_TEMPLATE;
    }
    return withoutLinks ? OTHERS_EMAIL_TEMPLATE_WITHOUT_LINKS : OTHERS_EMAIL_TEMPLATE;
  }

  public static String getSpecificTemplate(EmailNotificationRecipientType recipientType, boolean withoutLinks) {
    return switch (recipientType) {
      case OPERATOR -> withoutLinks ? OPERATOR_EMAIL_TEMPLATE_WITHOUT_LINKS : OPERATOR_EMAIL_TEMPLATE;
      case SPECIFIC_USER -> withoutLinks ? SPECIFIC_USER_EMAIL_TEMPLATE_WITHOUT_LINKS : SPECIFIC_USER_EMAIL_TEMPLATE;
      case OWN_PARTY -> withoutLinks ? OWN_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : OWN_PARTY_EMAIL_TEMPLATE;
      case COUNTER_PARTY -> withoutLinks ? COUNTER_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : COUNTER_PARTY_EMAIL_TEMPLATE;
      case ALL_PARTY -> withoutLinks ? ALL_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : ALL_PARTY_EMAIL_TEMPLATE;
    };
  }

  public static String getOperatorTemplate(boolean withoutLinks) {
    return withoutLinks ? OPERATOR_EMAIL_TEMPLATE_WITHOUT_LINKS : OPERATOR_EMAIL_TEMPLATE;
  }

  public static String getSpecificUserTemplate(boolean withoutLinks) {
    return withoutLinks ? SPECIFIC_USER_EMAIL_TEMPLATE_WITHOUT_LINKS : SPECIFIC_USER_EMAIL_TEMPLATE;
  }

  public static String getOwnPartyTemplate(boolean withoutLinks) {
    return withoutLinks ? OWN_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : OWN_PARTY_EMAIL_TEMPLATE;
  }

  public static String getCounterPartyTemplate(boolean withoutLinks) {
    return withoutLinks ? COUNTER_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : COUNTER_PARTY_EMAIL_TEMPLATE;
  }

  public static String getAllPartyTemplate(boolean withoutLinks) {
    return withoutLinks ? ALL_PARTY_EMAIL_TEMPLATE_WITHOUT_LINKS : ALL_PARTY_EMAIL_TEMPLATE;
  }

}
