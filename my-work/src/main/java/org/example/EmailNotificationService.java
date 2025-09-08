package org.example;

import org.example.EmailNotificationType.Config;

public class EmailNotificationService {

  public void sendMail(EmailNotificationType type, EmailNotificationParams params) {
    final Config config = type.getConfig();
    if (config.isSendToActionInitiator()) {
      System.out.println("处理发送给操作发起人的逻辑");
    }
    if (config.isSendToSpecificUser()) {
      System.out.println("处理发送给特定用户的逻辑");
    }
    if (config.isSendToAllParticipant()) {
      System.out.println("处理发送给所有参与者的逻辑");
    }
    if (config.isSendToCounterParticipant()) {
      System.out.println("处理发送给对方参与者的逻辑");
    }
    if (config.isSendToMyParticipant()) {
      System.out.println("处理发送给我方参与者的逻辑");
    }
  }
}
