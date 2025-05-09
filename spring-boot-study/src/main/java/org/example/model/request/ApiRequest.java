package org.example.model.request;

import lombok.Data;

@Data
public class ApiRequest<T> {

  private T data;
}
