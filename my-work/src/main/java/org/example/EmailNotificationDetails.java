package org.example;

import java.io.File;
import java.util.List;
import java.util.Map;
import lombok.Data;

@Data
public class EmailNotificationDetails {

  private EmailNotificationType notificationType;
  private List<String> actionInitiatorEmailAddressList;
  private List<String> specificUserEmailAddressList;
  private List<String> allParticipantEmailAddressList;
  private List<String> actionInitiatorParticipantEmailAddressList;
  private List<String> counterParticipantEmailAddressList;
  private List<File> attachments;
  private EmailContent actionInitiatorEmailTemplate;
  private EmailContent emailTemplate;
  private Map<String, String> templateBindings;
}
