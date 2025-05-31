package org.example.mybatis.model;

// 字段值包装类
public record FieldValue(String fieldName, String columnName, Object value) {

}