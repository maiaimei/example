package org.example.mybatis;

// 字段值包装类
public record FieldValue(String fieldName, String columnName, Object value) {

}