package org.example.service;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.JsonbTest;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.mybatis.service.AbstractBaseService;
import org.example.repository.JsonbTestRepository;
import org.example.utils.IdGenerator;
import org.springframework.stereotype.Service;

@Service
public class JsonbTestService extends AbstractBaseService<JsonbTest, JsonbTestRepository> {

  public JsonbTestService(JsonbTestRepository repository) {
    super(repository);
  }

  public List<JsonbTest> getJsonbTestList() {
    final JsonbTest jsonbTest = new JsonbTest();
    final List<Condition> conditions = QueryConditionBuilder.create()
        .andJsonTextEquals("data", "John", "name")
        .build();
    return advancedSelect(jsonbTest, conditions, null);
  }

  public JsonbTest getJsonbTest(BigDecimal id) {
    return selectById(id);
  }

  public JsonbTest createJsonbTest(JsonbTest jsonbTest) {
    jsonbTest.setId(IdGenerator.nextId());
    create(jsonbTest);
    return jsonbTest;
  }

  public JsonbTest updateJsonbTest(JsonbTest jsonbTest) {
    update(jsonbTest);
    return jsonbTest;
  }

  public void deleteJsonbTest(BigDecimal id) {
    deleteById(id);
  }
}
