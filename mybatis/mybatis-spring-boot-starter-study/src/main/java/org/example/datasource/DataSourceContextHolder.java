package org.example.datasource;

public class DataSourceContextHolder {

  private static final ThreadLocal<String> CONTEXT = new ThreadLocal<>();

  public static void set(String dataSourceName) {
    CONTEXT.set(dataSourceName);
  }

  public static String get() {
    return CONTEXT.get();
  }

  public static void clear() {
    CONTEXT.remove();
  }
}