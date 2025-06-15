package org.example.model.domain;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import lombok.Data;
import org.example.mybatis.annotation.TableColumn;
import org.example.mybatis.annotation.TableName;

@Data
@TableName("JSONB_TEST")
public class JsonbTest {

  private BigDecimal id;

  @TableColumn(type = "JSONB")
  private String stringData;

  @TableColumn(type = "JSONB")
  private Person personData;

  @TableColumn(type = "JSONB")
  private List<Person> personDataList;

  @TableColumn(type = "JSONB")
  private Map<String, Object> mapData;

  @TableColumn(type = "JSONB")
  private List<Map<String, Object>> mapDataList;
}
