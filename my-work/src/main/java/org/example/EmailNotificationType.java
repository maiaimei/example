package org.example;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
public enum EmailNotificationType {

  ;

  private final Config config;

  EmailNotificationType(Config config) {
    this.config = config;
  }

  @Data
  @Builder
  public static class Config {

    private boolean systemTriggered;
    private boolean accountLevelControlled;
    private boolean documentLevelControlled;
    private boolean sendToActionInitiator;
    private boolean sendToSpecificUser;
    private boolean sendToAllParticipants;
    private boolean sendToActionInitiatorParticipants;
    private boolean sendToCounterParticipants;
    private String accountSwitch;
    private String documentSwitch;

    public boolean hasNoLevelControl() {
      return !accountLevelControlled && !documentLevelControlled;
    }
  }
}
