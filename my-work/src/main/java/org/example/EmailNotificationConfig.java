package org.example;

import java.util.List;
import java.util.function.Predicate;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailNotificationConfig {

  private boolean systemTriggered;

  private boolean accountLevelControlled;
  private String accountSwitch;

  private boolean documentLevelControlled;
  private String documentSwitch;

  private boolean sendToActionInitiator;
  private boolean sendToActionInitiatorParticipants;
  private boolean sendToCounterParticipants;
  private boolean sendToAllParticipants;
  private boolean sendToSpecificUser;

  private Predicate<ParticipantInfo> counterParticipantPredicate;

  private String urlFormat;
  private List<String> imageResourceLocations;

  private boolean enableThrowingException;
  private String errorType;
  private String xMattersCode;

  public boolean isControlNotRequired() {
    return !accountLevelControlled && !documentLevelControlled;
  }
}