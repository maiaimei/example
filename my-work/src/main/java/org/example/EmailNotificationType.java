package org.example;

import lombok.Getter;

@Getter
public enum EmailNotificationType {

  INVITE_PARTICIPANT(EmailNotificationConfig.builder()
      .operatorUseSpecificTemplates(true)
      .operatorTemplateFunction(EmailNotificationUtils::getOperatorTemplate)
      .build());

  private final EmailNotificationConfig config;

  EmailNotificationType(EmailNotificationConfig config) {
    this.config = config;
  }


}
