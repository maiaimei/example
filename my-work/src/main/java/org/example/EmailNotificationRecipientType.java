package org.example;

import lombok.Getter;

@Getter
public enum EmailNotificationRecipientType {

  OPERATOR("operator", "action initiator"),

  SPECIFIC_USER("specific_user", "specific user"),

  OWN_PARTY("own_party", "action initiator's own party"),

  COUNTER_PARTY("counter_party", "action initiator's counter party"),

  ALL_PARTY("all_party", "all party");

  private final String code;
  private final String description;

  EmailNotificationRecipientType(String code, String description) {
    this.code = code;
    this.description = description;
  }

}
