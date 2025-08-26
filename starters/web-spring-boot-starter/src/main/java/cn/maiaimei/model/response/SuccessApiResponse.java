package cn.maiaimei.model.response;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 成功响应类，表示成功的响应
 *
 * @param <T> 响应数据类型
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SuccessApiResponse<T> implements BaseApiResponse {

  private Integer code;
  private String message;
  private T data;
  private String path;
  private String method;
  private LocalDateTime timestamp;
  private String correlationId;
}