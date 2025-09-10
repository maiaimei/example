package org.example;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import org.springframework.util.ResourceUtils;

public class MainApplication {

  public static void main(String[] args) throws FileNotFoundException {

    final EmailNotificationConfig notificationConfig = EmailNotificationType.INVITE_PARTICIPANT.getConfig();
    List<ParticipantInfo> participantInfoList = new ArrayList<>();
    participantInfoList.add(new ParticipantInfo("superAdministrator"));
    participantInfoList.add(new ParticipantInfo("regularAdministrator"));
    participantInfoList.add(new ParticipantInfo("viewer"));
    participantInfoList.stream().filter(notificationConfig.getCounterParticipantPredicate()).toList().forEach(System.out::println);

    final List<String> emailBodyImageLocations = notificationConfig.getEmailBodyImageLocations();
    for (String location : emailBodyImageLocations) {
      final File file = ResourceUtils.getFile(location);
      System.out.println(file.getAbsolutePath());
    }
  }
}
