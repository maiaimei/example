package org.example;

import lombok.Getter;

@Getter
public enum EmailNotificationRecipientType {

  OPERATOR("action initiator"),

  OWN_PARTY("action initiator's own party"),

  COUNTER_PARTY("action initiator's counter party"),

  ALL_PARTY("all party");

  private final String description;

  EmailNotificationRecipientType(String description) {
    this.description = description;
  }

}
