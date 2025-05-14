package org.example.model;

import lombok.Data;

@Data
public class ApiRequest<T> {

  private T data;
}
