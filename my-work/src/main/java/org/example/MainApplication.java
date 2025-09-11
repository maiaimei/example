package org.example;

import java.io.FileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class MainApplication {

  public static void main(String[] args) throws FileNotFoundException {

    final EmailNotificationConfig notificationConfig = EmailNotificationType.INVITE_PARTICIPANT.getConfig();


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

  public static void sendFileToApi(Resource fileResource) {
    // 创建 RestTemplate
    RestTemplate restTemplate = new RestTemplate();

    // 创建请求头
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);

    // 创建请求体
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();

    // 添加文件资源
    body.add("file", fileResource);
    // 添加其他表单字段
    body.add("field1", "value1");

    // 创建 HttpEntity
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

    // 发送请求
    String response = restTemplate.postForObject(
        "https://api.example.com/upload",
        requestEntity,
        String.class
    );
  }

}
