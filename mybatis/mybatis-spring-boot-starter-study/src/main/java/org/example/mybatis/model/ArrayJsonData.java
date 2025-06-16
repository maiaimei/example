package org.example.mybatis.model;

import java.util.List;
import lombok.Data;

@Data
public class ArrayJsonData<T> implements ArrayJsonAccessor<T> {

  private List<T> data;
}
