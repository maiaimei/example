package cn.maiaimei.example;

import cn.maiaimei.example.controller.PersonController;
import cn.maiaimei.example.model.Education;
import cn.maiaimei.example.model.Person;
import cn.maiaimei.example.util.ValidationResult;
import cn.maiaimei.example.util.ValidationUtils;
import org.junit.jupiter.api.Test;

import javax.validation.groups.Default;
import java.util.ArrayList;
import java.util.List;

public class ValidationTest {
    @Test
    void testBeanValid() {
        Person person = buildPerson();
        ValidationResult result = ValidationUtils.valid(person);
        printValidationResult(result);
    }

    @Test
    void testBeanValidByGroup() {
        Person person = buildPerson();
        //ValidationResult result = ValidationUtils.valid(person, Default.class, Person.Insert.class);
        ValidationResult result = ValidationUtils.valid(person, Default.class, Person.Update.class);
        printValidationResult(result);
    }

    @Test
    void testBeanValidFailFast() {
        Person person = buildPerson();
        ValidationResult result = ValidationUtils.validFailFast(person, Default.class, Person.Update.class);
        printValidationResult(result);
    }

    @Test
    void testNonBeanValidateParameters() {
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
        if (result.isHasErrors()) {
            result.getErrorMessages().forEach(System.out::println);
        }
    }
}
