package org.example;

import lombok.Getter;

@Getter
public enum EmailNotificationType {

  ;

  private final EmailNotificationConfig config;

  EmailNotificationType(EmailNotificationConfig config) {
    this.config = config;
  }


}
