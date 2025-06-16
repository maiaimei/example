package org.example.mybatis.model;

import java.util.List;
import lombok.Data;

@Data
public class ArrayJson<T> {

  private List<T> data;
}
