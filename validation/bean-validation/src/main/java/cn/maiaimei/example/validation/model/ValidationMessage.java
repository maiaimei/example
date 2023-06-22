package cn.maiaimei.example.validation.model;

import lombok.Data;

@Data
public class ValidationMessage {

  /**
   * the validation type, such as file, file content etc.
   */
  private String type;
  /**
   * the validation target, such as file name, bean property name, parameter name etc.
   */
  private String code;
  /**
   * alias or description for code, it is shown good readability for user
   */
  private String name;
  /**
   * original value
   */
  private Object value;
  /**
   * error message
   */
  private String description;
}
