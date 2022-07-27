package cn.maiaimei.example.controller;

import cn.maiaimei.example.config.RedisProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RefreshScope
@RestController
@RequestMapping("/config")
public class ConfigController {

    @Autowired
    private RedisProperties redisProperties;

    @Value("${spring.application.name}")
    private String name;

    @Value("${springfox.enabled:false}")
    private boolean enableSwagger;

    @Value("${springfox.documentation.swagger-ui.title:title}")
    private String title;

    @Value("${springfox.documentation.swagger-ui.description:description}")
    private String description;

    @GetMapping
    public Map<String, Object> get() {
        Map<String, String> redis = new HashMap<>();
        redis.put("host", redisProperties.getHost());
        redis.put("post", redisProperties.getPort());

        Map<String, Object> swagger = new HashMap<>();
        swagger.put("enabled", enableSwagger);
        swagger.put("title", title);
        swagger.put("description", description);

        Map<String, Object> cfg = new HashMap<>();
        cfg.put("name", name);
        cfg.put("redis", redis);
        cfg.put("swagger", swagger);
        return cfg;
    }

}