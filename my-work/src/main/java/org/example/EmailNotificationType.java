package org.example;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

// EmailController
// EmailNotificationService
// EmailNotificationTemplateService
@Getter
public enum EmailNotificationType {

  INVITE_PARTICIPANT(
      Config.builder()
          .sendToActionInitiator(true)
          .sendToSpecificUser(true)
          .build()
  );

  private final Config config;

  EmailNotificationType(Config config) {
    this.config = config;
  }

  @Data
  @Builder
  public static class Config {

    private boolean systemTriggered;
    private boolean controlByDocumentLevel;
    private String controlByDocumentLevelKey;
    private boolean controlByProfileLevel;
    private String controlByProfileLevelKey;
    private boolean sendToActionInitiator;
    private boolean sendToSpecificUser;
    private boolean sendToAllParticipant;
    private boolean sendToCounterParticipant;
    private boolean sendToMyParticipant;
  }
}
