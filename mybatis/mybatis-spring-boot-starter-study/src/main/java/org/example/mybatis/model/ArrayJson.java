package org.example.mybatis.model;

import java.util.List;
import lombok.Data;

@Data
public class ArrayJson<T> {

  private List<T> data;

  // 静态方法：返回 ArrayJson 或其子类实例
  public static <T, S extends ArrayJson<T>> S of(List<T> data, Class<S> clazz) {
    try {
      S instance = clazz.getDeclaredConstructor().newInstance();
      instance.setData(data);
      return instance;
    } catch (Exception e) {
      throw new RuntimeException("Failed to create instance of " + clazz.getName(), e);
    }
  }
}
