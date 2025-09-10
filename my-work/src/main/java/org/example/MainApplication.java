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

    final List<String> emailBodyImageLocations = notificationConfig.getImageResourceLocations();
    for (String location : emailBodyImageLocations) {
      final File file = ResourceUtils.getFile(location);
      System.out.println(file.getAbsolutePath());
    }
  }

  public void processEmailNotification(EmailNotificationDTO dto) {
    // 将 DTO 转换为 Details 对象
    EmailNotificationDetails details = convertToDetails(dto);
    // 处理邮件通知逻辑
  }

  private EmailNotificationDetails convertToDetails(EmailNotificationDTO dto) {
    EmailNotificationDetails details = new EmailNotificationDetails();
    // 设置来自 DTO 的字段
    details.setNotificationType(dto.getNotificationType());
    details.setSpecificUserEmailAddressList(dto.getSpecificUserEmailAddressList());
    // 设置其他技术实现所需的字段
    return details;
  }
}
