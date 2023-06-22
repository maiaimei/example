package cn.maiaimei.example.validation.model;

import java.util.ArrayList;
import java.util.List;

public class ValidationResult {

  private final List<ValidationMessage> validationMessages;

  public ValidationResult() {
    validationMessages = new ArrayList<>();
  }

  public boolean hasErrors() {
    return !validationMessages.isEmpty();
  }

  public List<ValidationMessage> getAllErrors() {
    return validationMessages;
  }

  public void addError(ValidationMessage validationMessage) {
    this.validationMessages.add(validationMessage);
  }
}
