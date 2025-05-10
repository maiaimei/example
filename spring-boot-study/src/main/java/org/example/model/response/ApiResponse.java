package org.example.model.response;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ApiResponse<T> {

  /**
   * 响应的状态码或错误代码
   */
  private Integer code;

  /**
   * 成功的简短描述或详细信息
   */
  private String message;

  /**
   * 消息key，用于前端国际化
   */
  private String messageKey;

  /**
   * 当请求成功时，这里通常包含请求的数据。在错误响应中，这个字段可以包含额外的错误详情或修正建议。
   */
  private T data;

  /**
   * 响应生成的时间戳
   */
  private Long timestamp;

  private String path;
  private BigDecimal traceId;
}
