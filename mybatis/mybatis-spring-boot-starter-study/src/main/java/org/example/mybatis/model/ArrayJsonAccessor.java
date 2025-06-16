package org.example.mybatis.model;

import java.util.List;

/**
 * 数组形式的 JSON 访问接口
 *
 * @param <T> 数组元素类型
 */
public interface ArrayJsonAccessor<T> {

  /**
   * 获取数据列表
   *
   * @return 数据列表
   */
  List<T> getData();

  /**
   * 设置数据列表
   *
   * @param data 数据列表
   */
  void setData(List<T> data);
}

