package org.example;

import lombok.Getter;

@Getter
public enum EmailNotificationType {

  SEND_REGISTRATION_VERIFICATION_CODE(EmailNotificationConfig.builder()
      .systemTriggered(true)
      .nonWordingRelated(true)
      .useDefaultTpl(true)
      .build()),

  INVITE_PARTICIPANT(EmailNotificationConfig.builder()
      .useOthersTplExceptOperator(true)
      .tplIncludeProduct(false)
      .tplIncludeLocationInstitution(false)
      .tplIncludePartyType(false)
      .tplIncludeUserCategory(false)
      .operatorTplIgnoreLinksFlag(true)
      //.specificUserTplIgnoreLinksFlag(true)
      .build());

  private final EmailNotificationConfig config;

  EmailNotificationType(EmailNotificationConfig config) {
    this.config = config;
  }


}
