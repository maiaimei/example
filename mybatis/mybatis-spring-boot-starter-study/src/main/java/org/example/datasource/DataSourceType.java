package org.example.datasource;

import lombok.Getter;

@Getter
public enum DataSourceType {

  ORACLE("oracle"),

  MYSQL("mysql"),

  POSTGRESQL("postgresql");

  private final String type;

  DataSourceType(String type) {
    this.type = type;
  }
}
