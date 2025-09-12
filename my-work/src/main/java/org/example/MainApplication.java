package org.example;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MainApplication {

  public static void main(String[] args) {

    final EmailNotificationConfig config = EmailNotificationType.INVITE_PARTICIPANT.getConfig();
    for (EmailNotificationRecipientType recipientType : EmailNotificationRecipientType.values()) {
      String product = "GTE";
      String locationInstitution = "HKTEST";
      String partyType = "BANK";
      String userCategory = "STAFF";
      boolean withoutLinks = true;
      final String templateName = EmailNotificationTemplateName.getTemplateName(config, recipientType, product, locationInstitution,
          partyType, userCategory, withoutLinks);
      log.info("{} use {} template", recipientType.getDescription(), templateName);
    }
  }

}
