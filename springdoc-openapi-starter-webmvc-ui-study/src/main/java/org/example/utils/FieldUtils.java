package org.example.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class FieldUtils {

  /**
   * 获取类及其父类的所有字段
   *
   * @param clazz 目标类
   * @return 所有字段列表
   */
  public static List<Field> getAllFields(Class<?> clazz) {
    List<Field> fields = new ArrayList<>();

    // 遍历类层次结构
    while (clazz != null) {
      // 获取当前类的声明字段
      fields.addAll(Arrays.asList(clazz.getDeclaredFields()));
      // 获取父类
      clazz = clazz.getSuperclass();
    }

    return fields;
  }

  /**
   * 获取类及其父类的所有字段
   *
   * @param clazz          目标类
   * @param includePrivate 是否包含私有字段
   * @param includeStatic  是否包含静态字段
   * @param includeFinal   是否包含final字段
   * @return 符合条件的字段列表
   */
  public static List<Field> getAllFields(Class<?> clazz,
      boolean includePrivate,
      boolean includeStatic,
      boolean includeFinal) {
    List<Field> fields = new ArrayList<>();

    while (clazz != null) {
      for (Field field : clazz.getDeclaredFields()) {
        int modifiers = field.getModifiers();

        // 根据条件过滤字段
        if (!includePrivate && Modifier.isPrivate(modifiers)) {
          continue;
        }
        if (!includeStatic && Modifier.isStatic(modifiers)) {
          continue;
        }
        if (!includeFinal && Modifier.isFinal(modifiers)) {
          continue;
        }

        fields.add(field);
      }
      clazz = clazz.getSuperclass();
    }

    return fields;
  }

  /**
   * 获取指定类型的字段
   *
   * @param clazz     目标类
   * @param fieldType 字段类型
   * @return 指定类型的字段列表
   */
  public static <T> List<Field> getFieldsByType(Class<?> clazz, Class<T> fieldType) {
    return getAllFields(clazz, true, true, true)
        .stream()
        .filter(field -> fieldType.isAssignableFrom(field.getType()))
        .collect(Collectors.toList());
  }

}

