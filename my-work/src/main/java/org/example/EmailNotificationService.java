package org.example;

import java.util.ArrayList;
import java.util.List;

public class EmailNotificationService {

  public void sendMail(EmailNotificationParams notificationParams) {
    preSendMail(notificationParams);
    doSendMail(notificationParams);
  }

  private void preSendMail(EmailNotificationParams notificationParams) {
    EmailNotificationType notificationType = notificationParams.getNotificationType();
    EmailNotificationConfig notificationConfig = notificationType.getConfig();

    if (shouldProcessNotification(notificationConfig)) {
      processNotificationParams(notificationParams, notificationConfig);
    }
  }

  private void doSendMail(EmailNotificationParams notificationParams) {

  }

  private boolean shouldProcessNotification(EmailNotificationConfig notificationConfig) {
    return notificationConfig.isUncontrolled()
        || notificationConfig.isAccountLevelControlled()
        || isDocumentLevelControlEnabled(notificationConfig);
  }

  private void processNotificationParams(EmailNotificationParams notificationParams, EmailNotificationConfig notificationConfig) {
    if (notificationConfig.isSendToActionInitiator()) {
      notificationParams.setActionInitiatorEmailAddressList(getActionInitiatorEmailAddressList());
      notificationParams.setActionInitiatorEmailTemplate(null);
    }

    boolean shouldProcessParticipants = shouldProcessParticipants(notificationConfig);
    if (notificationConfig.isSendToSpecificUser() || shouldProcessParticipants) {
      notificationParams.setEmailTemplate(null);
    }

    if (shouldProcessParticipants) {
      processParticipants(notificationParams, notificationConfig);
    }
  }

  private boolean shouldProcessParticipants(EmailNotificationConfig notificationConfig) {
    return notificationConfig.isSendToAllParticipants()
        || notificationConfig.isSendToActionInitiatorParticipants()
        || notificationConfig.isSendToCounterParticipants();
  }

  private void processParticipants(EmailNotificationParams notificationParams, EmailNotificationConfig notificationConfig) {
    if (notificationConfig.isSendToAllParticipants()) {
      notificationParams.setAllParticipantEmailAddressList(null);
    }
    if (notificationConfig.isSendToActionInitiatorParticipants()) {
      notificationParams.setActionInitiatorParticipantEmailAddressList(null);
    }
    if (notificationConfig.isSendToCounterParticipants()) {
      notificationParams.setCounterParticipantEmailAddressList(null);
    }
  }

  private void filterEmailAddressIfAccountLevelControlled(EmailNotificationParams notificationParams,
      EmailNotificationConfig notificationConfig) {
    if (notificationConfig.isAccountLevelControlled()) {
      List<String> emailAddress = new ArrayList<>();
    }
  }

  private void putEmailAddressIfNotEmpty(List<String> emailAddressList, List<String> targetEmailAddressList) {
    
  }

  private boolean isDocumentLevelControlEnabled(EmailNotificationConfig notificationConfig) {
    final String documentSwitch = notificationConfig.getDocumentSwitch();
    return notificationConfig.isDocumentLevelControlled();
  }

  private List<String> getActionInitiatorEmailAddressList() {
    return null;
  }
}
