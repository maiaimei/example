package org.example.mybatis.query;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import org.example.mybatis.SQLOperator;

// 定义通用查询参数基类
@Data
public class BaseQuery {

  private List<QueryCondition> conditions = new ArrayList<>();
  private String sortField;
  private String sortOrder;
  private Integer pageNum;
  private Integer pageSize;

  public void addCondition(String column, SQLOperator operator, Object value) {
    conditions.add(new QueryCondition(column, operator, value));
  }

  public void addBetweenCondition(String column, Object value1, Object value2) {
    conditions.add(new QueryCondition(column, SQLOperator.BETWEEN, value1, value2));
  }
}
