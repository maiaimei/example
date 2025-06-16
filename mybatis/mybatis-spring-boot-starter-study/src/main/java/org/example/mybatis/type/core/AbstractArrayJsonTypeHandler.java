package org.example.mybatis.type.core;

import com.fasterxml.jackson.core.type.TypeReference;
import java.sql.*;
import java.util.List;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.example.mybatis.model.ArrayJson;
import org.example.utils.JsonUtils;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

/**
 * Java list-form object type and database array-form JSON type conversion handler
 * 数组形式的 JSON 类型处理器
 *
 * @param <T> 数组元素类型
 */
@Slf4j
public class AbstractArrayJsonTypeHandler<A extends ArrayJson<T>, T> extends BaseTypeHandler<A> {

  private final Class<A> type;

  public AbstractArrayJsonTypeHandler(Class<A> type) {
    if (Objects.isNull(type)) {
      throw new IllegalArgumentException("Type argument cannot be null");
    }
    this.type = type;
  }

  @Override
  public void setNonNullParameter(PreparedStatement ps, int i, A parameter, JdbcType jdbcType)
      throws SQLException {
    if (Objects.isNull(parameter) || CollectionUtils.isEmpty(parameter.getData())) {
      ps.setNull(i, jdbcType == null ? Types.VARCHAR : jdbcType.TYPE_CODE);
      return;
    }
    String json = JsonUtils.toJson(parameter.getData());
    ps.setString(i, json);
  }

  @Override
  public A getNullableResult(ResultSet rs, String columnName) throws SQLException {
    return toObject(rs.getString(columnName));
  }

  @Override
  public A getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
    return toObject(rs.getString(columnIndex));
  }

  @Override
  public A getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
    return toObject(cs.getString(columnIndex));
  }

  private A toObject(String json) {
    if (!StringUtils.hasText(json)) {
      return null;
    }

    try {
      // 使用反射创建具体的实现类实例
      A arrayJson = type.getDeclaredConstructor().newInstance();

      // 将 JSON 字符串转换为 List<T>
      List<T> dataList = JsonUtils.toObject(json, new TypeReference<>() {
      });

      // 设置数据
      arrayJson.setData(dataList);

      return arrayJson;
    } catch (Exception e) {
      log.error("Failed to convert JSON to ArrayJson. Type: {}, JSON: {}", type.getName(), json, e);
      return null;
    }
  }

}

