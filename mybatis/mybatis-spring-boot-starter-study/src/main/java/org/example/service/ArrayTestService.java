package org.example.service;

import java.math.BigDecimal;
import java.util.List;
import org.example.model.domain.ArrayTest;
import org.example.mybatis.query.filter.Condition;
import org.example.mybatis.query.filter.QueryConditionBuilder;
import org.example.mybatis.service.AbstractBaseService;
import org.example.repository.ArrayTestRepository;
import org.example.utils.IdGenerator;
import org.springframework.stereotype.Service;

@Service
public class ArrayTestService extends AbstractBaseService<ArrayTest, ArrayTestRepository> {

  public ArrayTestService(ArrayTestRepository repository) {
    super(repository);
  }

  public List<ArrayTest> getArrayTestList() {
    final ArrayTest arrayTest = new ArrayTest();
    final List<Condition> conditions = QueryConditionBuilder.create().build();
    return advancedSelect(arrayTest, conditions, null);
  }

  public ArrayTest getArrayTest(BigDecimal id) {
    return selectById(id);
  }

  public ArrayTest createArrayTest(ArrayTest arrayTest) {
    arrayTest.setId(IdGenerator.nextId());
    create(arrayTest);
    return arrayTest;
  }

  public ArrayTest updateArrayTest(ArrayTest arrayTest) {
    update(arrayTest);
    return arrayTest;
  }

  public void deleteArrayTest(BigDecimal id) {
    deleteById(id);
  }
}
