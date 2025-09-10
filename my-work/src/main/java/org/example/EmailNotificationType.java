package org.example;

import java.util.List;
import lombok.Getter;

@Getter
public enum EmailNotificationType {

  INVITE_PARTICIPANT(EmailNotificationConfig.builder()
      .counterParticipantPredicate(
          participantInfo -> List.of("regularAdministrator", "superAdministrator").contains(participantInfo.getRole()))
      .imageClassPaths(List.of("static/image.png"))
      .build());

  private final EmailNotificationConfig config;

  EmailNotificationType(EmailNotificationConfig config) {
    this.config = config;
  }


}
