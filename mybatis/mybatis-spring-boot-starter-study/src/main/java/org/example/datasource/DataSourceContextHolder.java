package org.example.datasource;

public class DataSourceContextHolder {

  private static final ThreadLocal<String> DATA_SOURCE_NAME_CONTEXT = new ThreadLocal<>();

  private static final ThreadLocal<String> DATA_SOURCE_TYPE_CONTEXT = new ThreadLocal<>();

  public static void setDataSourceName(String name) {
    DATA_SOURCE_NAME_CONTEXT.set(name);
  }

  public static String getDataSourceName() {
    return DATA_SOURCE_NAME_CONTEXT.get();
  }

  public static void clearDataSourceName() {
    DATA_SOURCE_NAME_CONTEXT.remove();
  }

  public static void setDataSourceType(String type) {
    DATA_SOURCE_TYPE_CONTEXT.set(type);
  }

  public static String getDataSourceType() {
    return DATA_SOURCE_TYPE_CONTEXT.get();
  }

  public static void clearDataSourceType() {
    DATA_SOURCE_TYPE_CONTEXT.remove();
  }
}