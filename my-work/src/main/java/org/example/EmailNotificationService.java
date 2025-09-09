package org.example;

import org.example.EmailNotificationType.Config;

public class EmailNotificationService {

  public void sendMail(EmailNotificationParams params) {
    EmailNotificationType emailNotificationType = params.getEmailNotificationType();
    Config config = emailNotificationType.getConfig();

  }
}
