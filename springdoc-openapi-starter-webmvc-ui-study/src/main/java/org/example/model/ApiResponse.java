package org.example.model;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.HttpStatus;

public class ApiResponse {

  public static <T> BaseResponse<T> success(T data) {
    return new SuccessResponse<T>()
        .setCode(HttpStatus.OK.value())
        .setData(data);
  }

  public static <T> BaseResponse<T> error() {
    return new ErrorResponse<T>()
        .setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
  }

  public interface BaseResponse<T> {

  }

  @Data
  @Accessors(chain = true)
  @Schema(description = "The success response")
  public static class SuccessResponse<T> implements BaseResponse<T> {

    @Schema(description = "The code of response", example = "200")
    private Integer code;

    @Schema(description = "The message of response", example = "success")
    private String message;

    @Schema(description = "The data of response")
    private T data;

    @Schema(description = "The timestamp of response", example = "2025-05-15T21:14:26.858234600")
    private LocalDateTime timestamp;

    @Schema(description = "The path of request", example = "/examples")
    private String path;

    @Schema(description = "The method of request", example = "GET")
    private String method;

    @Schema(description = "The correlation id of response", example = "2025051522504809400001")
    private String correlationId;
  }

  @Data
  @Accessors(chain = true)
  @Schema(description = "The error response")
  public static class ErrorResponse<T> implements BaseResponse<T> {

    @Schema(description = "The code of response", example = "500")
    private Integer code;

    @Schema(description = "The message of response", example = "Error")
    private String message;

    @Schema(description = "The data of response")
    private T data;

    @Schema(description = "The error of response", example = "This is error")
    private String error;

    @Schema(description = "The details of response", example = "This is details")
    private String details;

    @Schema(description = "The timestamp of response", example = "2025-05-15T21:14:26.858234600")
    private LocalDateTime timestamp;

    @Schema(description = "The path of request", example = "/examples")
    private String path;

    @Schema(description = "The method of request", example = "GET")
    private String method;

    @Schema(description = "The correlation id of response", example = "2025051522504809400001")
    private String correlationId;
  }
}
