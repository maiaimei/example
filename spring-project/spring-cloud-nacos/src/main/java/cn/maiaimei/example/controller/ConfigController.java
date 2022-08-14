package cn.maiaimei.example.controller;

import cn.maiaimei.example.config.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private RedisProperties redisProperties;

    @Value("${spring.application.name}")
    private String name;

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @GetMapping
    public Map<String, Object> get() {
        Map<String, String> redis = new LinkedHashMap<>();
        redis.put("host", redisProperties.getHost());
        redis.put("post", redisProperties.getPort());

        Map<String, Object> datasource = new LinkedHashMap<>();
        datasource.put("url", url);
        datasource.put("username", username);
        datasource.put("password", password);

        Map<String, Object> cfg = new LinkedHashMap<>();
        cfg.put("name", name);
        cfg.put("redis", redis);
        cfg.put("datasource", datasource);
        return cfg;
    }

}