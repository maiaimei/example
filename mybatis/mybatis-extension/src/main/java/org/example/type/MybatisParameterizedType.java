package org.example.type;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 自定义ParameterizedType实现，用于注册具体的泛型类型
 */
public class MybatisParameterizedType implements ParameterizedType {

  private final Class<?> rawType;
  private final Type[] actualTypeArguments;

  public MybatisParameterizedType(Class<?> rawType, Class<?>... actualTypeArguments) {
    this.rawType = rawType;
    this.actualTypeArguments = actualTypeArguments;
  }

  @Override
  public Type[] getActualTypeArguments() {
    return actualTypeArguments;
  }

  @Override
  public Type getRawType() {
    return rawType;
  }

  @Override
  public Type getOwnerType() {
    return null;
  }
}