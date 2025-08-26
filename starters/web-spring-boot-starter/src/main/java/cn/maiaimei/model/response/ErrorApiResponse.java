package cn.maiaimei.model.response;

import cn.maiaimei.model.response.error.ErrorField;
import cn.maiaimei.model.response.error.ErrorInfo;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;
import lombok.Data;

/**
 * 错误响应类，表示错误的响应
 *
 * @param <T> 响应数据类型
 */
@Data
@Builder
public class ErrorApiResponse<T> implements BaseApiResponse {

  private Integer code;
  private String message;
  private T data;
  private ErrorInfo error;
  private List<ErrorField> details;
  private String path;
  private String method;
  private LocalDateTime timestamp;
  private String correlationId;
}