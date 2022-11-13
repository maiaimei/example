package cn.maiaimei.example;

import cn.maiaimei.example.mapper.DbMapper;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Application {
    private static final String PREFIX = "sys_";
    private static final String FORMAT = "<table tableName=\"%s\" domainObjectName=\"%s\"/>";

    public static void main(String[] args) throws IOException {
        String resource = "mybatis-config.xml";
        InputStream inputStream = Resources.getResourceAsStream(resource);
        SqlSessionFactory sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        DbMapper dbMapper = sqlSession.getMapper(DbMapper.class);
        List<String> tableNames = dbMapper.selectAllTableNamesFromMySql("testdb1");
        for (String tableName : tableNames) {
            // 过滤表
            if (tableName.contains(PREFIX)) {
                // 去掉前缀，以_分隔单词
                String[] arr = tableName.replaceAll(PREFIX, "").split("_");
                // 每个单词首字母大写
                List<String> list = Arrays.stream(arr).map(i -> i.substring(0, 1).toUpperCase().concat(i.substring(1).toLowerCase())).collect(Collectors.toList());
                // Java实体名
                String domainObjectName = String.join("", list);
                System.out.printf(FORMAT, tableName, domainObjectName);
                System.out.println();
            }
        }
    }
}
