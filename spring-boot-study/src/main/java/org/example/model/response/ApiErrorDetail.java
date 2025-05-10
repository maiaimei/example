package org.example.model.response;

import lombok.Data;

@Data
public class ApiErrorDetail {

  private String field;

  /**
   * 字段key，用于前端国际化
   */
  private String fieldKey;

  private String message;

  /**
   * 消息key，用于前端国际化
   */
  private String messageKey;

}
