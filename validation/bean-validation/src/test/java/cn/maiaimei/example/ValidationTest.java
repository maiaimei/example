package cn.maiaimei.example;

import cn.maiaimei.example.controller.PersonController;
import cn.maiaimei.example.model.Education;
import cn.maiaimei.example.model.People;
import cn.maiaimei.example.model.Person;
import cn.maiaimei.example.validation.model.ValidationResult;
import cn.maiaimei.example.validation.utils.ValidationUtils;
import java.util.ArrayList;
import java.util.List;
import javax.validation.groups.Default;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

@Slf4j
public class ValidationTest {

  @Test
  void testValidateByAnnotation() {
    final Person person = new Person();
    ValidationResult result = ValidationUtils.validate(person);
    printValidationResult(result);
  }

  @Test
  void testValidateByXML() {
    final People people = new People();
    people.setSex("T");
    ValidationResult result = ValidationUtils.validate(people);
    printValidationResult(result);
  }

  @Test
  void testValidateBean() {
    Person person = buildPerson();
    ValidationResult result = ValidationUtils.validate(person);
    printValidationResult(result);
  }

  @Test
  void testValidateBeanByGroup() {
    Person person = buildPerson();
    //ValidationResult result = ValidationUtils.valid(person, Default.class, Person.Insert.class);
    ValidationResult result = ValidationUtils.validate(person, Default.class, Person.Update.class);
    printValidationResult(result);
  }

  @Test
  void testFailFastValidateBean() {
    Person person = buildPerson();
    ValidationResult result = ValidationUtils.failFastValidate(person, Default.class,
        Person.Update.class);
    printValidationResult(result);
  }

  @Test
  void testValidateParameters() {
    PersonController personController = new PersonController();
    personController.delete(null);
  }

  private Person buildPerson() {
    Education education = new Education();

    List<Education> educations = new ArrayList<>();
    educations.add(education);

    Person person = new Person();
    person.setSex("x");
    person.setAge(160);
    person.setEducation(educations);
    return person;
  }

  private void printValidationResult(ValidationResult result) {
    if (result.hasErrors()) {
      result.getAllErrors().forEach(validationMessage -> {
        log.info("Error Code: {}", validationMessage.getCode());
        log.info("Error Message: {}", validationMessage.getDescription());
        log.info("Error Value: {}", validationMessage.getValue());
      });
    }
  }
}
