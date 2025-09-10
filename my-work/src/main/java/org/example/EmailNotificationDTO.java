package org.example;

import java.util.List;
import lombok.Data;

@Data
public class EmailNotificationDTO {

  private EmailNotificationType notificationType;
  private List<String> specificUserEmailAddressList;
}
