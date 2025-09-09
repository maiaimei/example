package org.example;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationConfig {

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

  public boolean isUncontrolled() {
    return !accountLevelControlled && !documentLevelControlled;
  }
}