package org.example.mybatis.type;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.example.mybatis.model.ArrayJsonAccessor;
import org.example.mybatis.model.ArrayJsonData;
import org.example.utils.JsonUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * 数组形式的 JSON 类型处理器
 *
 * @param <T> 数组元素类型
 */
@Slf4j
public class ArrayJsonTypeHandler<T> extends BaseTypeHandler<ArrayJsonAccessor<T>> {

  private final Class<T> type;

  public ArrayJsonTypeHandler(Class<T> type) {
    if (Objects.isNull(type)) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, ArrayJsonAccessor<T> parameter, JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(parameter) || CollectionUtils.isEmpty(parameter.getData())) {
      ps.setNull(i, jdbcType == null ? Types.VARCHAR : jdbcType.TYPE_CODE);
      return;
    }
    String json = JsonUtils.toJson(parameter.getData());
    ps.setString(i, json);
  }

  @Override
  public ArrayJsonAccessor<T> getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toObject(rs.getString(columnName));
  }

  @Override
  public ArrayJsonAccessor<T> getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toObject(rs.getString(columnIndex));
  }

  @Override
  public ArrayJsonAccessor<T> getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toObject(cs.getString(columnIndex));
  }

  private ArrayJsonAccessor<T> toObject(String json) {
    if (!StringUtils.hasText(json)) {
      return null;
    }

    try {
      // 创建一个具体的实现类实例
      ArrayJsonData<T> arrayJsonData = new ArrayJsonData<>();

      // 将 JSON 字符串转换为 List<T>
      List<T> dataList = JsonUtils.toObject(json,
          JsonUtils.getCollectionType(ArrayList.class, type));

      // 设置数据
      arrayJsonData.setData(dataList);

      return arrayJsonData;
    } catch (Exception e) {
      // 记录日志但不抛出异常，返回 null 表示转换失败
      log.error("Failed to convert JSON to ArrayJsonAccessor: " + json, e);
      return null;
    }
  }

}

