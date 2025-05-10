package org.example.model.response;

import java.util.List;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApiErrorResponse<T> extends ApiResponse<T> {
  private ApiError error;

  private List<ApiErrorDetail> details;
}
