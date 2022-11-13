package cn.maiaimei.example;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.util.Collections;

public class Application {
    private static final String URL = "jdbc:mysql://192.168.1.12/testdb1?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=GMT%2B8";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "123456";
    private static final String AUTHOR = "maiaimei";
    private static final String PACKAGE_NAME = "cn.maiaimei.example";
    private static final String JAVA_DIR = "E:\\app\\tmp\\java";
    private static final String XML_DIR = "E:\\app\\tmp\\resources";


    public static void main(String[] args) {
        FastAutoGenerator.create(URL, USERNAME, PASSWORD)
                .globalConfig(builder -> {
                    builder.author(AUTHOR)
                            // 开启 swagger 模式
                            .enableSwagger()
                            // 覆盖已生成文件
                            .fileOverride()
                            // 指定输出目录
                            .outputDir(JAVA_DIR);
                })
                .packageConfig(builder -> {
                    // 设置父包名
                    builder.parent(PACKAGE_NAME)
                            // 设置mapperXml生成路径
                            .pathInfo(Collections.singletonMap(OutputFile.xml, XML_DIR));
                })
                .strategyConfig(builder -> {
                    // 设置需要生成的表名
                    builder.addInclude("sys_user")
                            // 设置过滤表前缀
                            .addTablePrefix("t_", "c_");
                })
                // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .templateEngine(new FreemarkerTemplateEngine())
                .execute();
    }
}
