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
//        .andEquals("id", new BigDecimal("2025061512423775300003"))
//        .andJsonbTextEquals("stringData", "John Doe", "name")
//        .andJsonbTextLike("stringData", "John", "name")
//        .andJsonbTextNotLike("stringData", "Tom", "name")
//        .andJsonbArrayContains("stringData", "important", "tags")
//        .andJsonbArrayLike("stringData", "view", "tags")
//        .andJsonbObjectArrayEquals("stringData", "Product1", "items", "name")
//        .andJsonbObjectArrayLike("stringData", "Product", "items", "name")
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

  public void updateName(BigDecimal id, String name) {
    getRepository().updateName(id, name);
  }

  public void updateContactAddress(BigDecimal id, String contactAddress) {
    getRepository().updateContactAddress(id, contactAddress);
  }

  public void addTag(BigDecimal id, String tag) {
    getRepository().addTag(id, tag);
  }
}
