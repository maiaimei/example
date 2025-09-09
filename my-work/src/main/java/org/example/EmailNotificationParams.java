package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class EmailNotificationParams {

  private EmailNotificationType emailNotificationType;
  private List<String> actionInitiatorEmailAddressList;
  private List<String> specificUserEmailAddressList;
  private List<String> allParticipantEmailAddressList;
  private List<String> actionInitiatorParticipantEmailAddressList;
  private List<String> counterParticipantEmailAddressList;
  private List<File> attachments;
  private String initiatorEmailTemplate;
  private String emailTemplate;
  private Map<String, String> templateBindings;
}
