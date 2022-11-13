package cn.maiaimei.example.mapper;

import java.util.List;

public interface DbMapper {
    List<String> selectAllTableNamesFromMySql(String dbName);
}
