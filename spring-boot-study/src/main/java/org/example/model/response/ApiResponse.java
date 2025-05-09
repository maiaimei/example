package org.example.model.response;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class ApiResponse<T> {

  /**
   * 响应的状态码或错误代码
   */
  private Integer code;

  /**
   * 当请求成功时，这里通常包含请求的数据。在错误响应中，这个字段可以包含额外的错误详情或修正建议。
   */
  private T data;

  /**
   * 响应生成的时间戳
   */
  private LocalDateTime timestamp;

  private String path;
  private BigDecimal traceId;
}
