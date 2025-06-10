package org.example.mybatis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * FieldMetadata wrapper class.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class FieldMetadata {

  /**
   * Name of the field in the Java object
   */
  private String fieldName;

  /**
   * Name of the column in the database
   */
  private String columnName;

  /**
   * Type of the column in the database
   */
  private String columnType;

  /**
   * Value of the field
   */
  private Object value;
}