[https://spring.io/projects/spring-cloud-alibaba](https://spring.io/projects/spring-cloud-alibaba)

[https://github.com/alibaba/spring-cloud-alibaba](https://github.com/alibaba/spring-cloud-alibaba)

[https://github.com/alibaba/spring-cloud-alibaba/wiki/版本说明](https://github.com/alibaba/spring-cloud-alibaba/wiki/%E7%89%88%E6%9C%AC%E8%AF%B4%E6%98%8E)

[https://nacos.io/zh-cn/docs/quick-start.html](https://nacos.io/zh-cn/docs/quick-start.html)

[Nacos视频教程（无废话版）](https://www.bilibili.com/video/BV1WZ4y1w7ww)

# 启动与关闭 Nacos server

```shell
tar -xvf nacos-server-$version.tar.gz
cd /opt/nacos/bin

# 启动命令(standalone代表着单机模式运行，非集群模式)
sh startup.sh -m standalone

# 关闭服务
sh shutdown.sh
```

# 配置中心

## 定位配置

```
${prefix}-${spring.profiles.active}.${file-extension}
```

- prefix 默认为 spring.application.name 的值，也可以通过配置项 spring.cloud.nacos.config.prefix来配置。
- spring.profiles.active 即为当前环境对应的 profile，详情可以参考 Spring Boot文档。 注意：当 spring.profiles.active 为空时，对应的连接符 - 也将不存在，dataId 的拼接格式变成 ${prefix}.${file-extension}
- file-exetension 为配置内容的数据格式，可以通过配置项 spring.cloud.nacos.config.file-extension 来配置。目前只支持 properties 和 yaml 类型。

```yaml
spring:
  application:
    # 配置应用名
    name: spring-cloud-nacos
  cloud:
    nacos:
      config:
        # 配置 Nacos server 的地址
        server-addr: 192.168.1.23:8848
        # namespace, separation configuration of different environments. e.g. dev
        namespace: 864ee127-efe0-482f-bd7f-8ea7bd74d63d
        group: DEFAULT_GROUP
        # nacos config dataId name.
        name: spring-cloud-nacos
        file-extension: yaml
```

### 单配置集

```yaml
spring:
  application:
    # 配置应用名
    name: spring-cloud-nacos
  cloud:
    nacos:
      config:
        # 配置 Nacos server 的地址
        server-addr: 192.168.1.23:8848
        # nacos config dataId name.
        name: spring-cloud-nacos
        file-extension: yaml
        # namespace, separation configuration of different environments. e.g. dev
        namespace: 864ee127-efe0-482f-bd7f-8ea7bd74d63d
        group: DEFAULT_GROUP
```

### 多配置集

```yaml
spring:
  application:
    # 配置应用名
    name: spring-cloud-nacos
  cloud:
    nacos:
      config:
        # 配置 Nacos server 的地址
        server-addr: 192.168.1.23:8848
        # namespace, separation configuration of different environments. e.g. dev
        namespace: 864ee127-efe0-482f-bd7f-8ea7bd74d63d
        # 多配置集
        extension-configs:
          - group: DEFAULT_GROUP
            # 多配置集，dataId要加上文件后缀，否则无法读取配置？？
            data-id: spring-cloud-nacos-swagger.yaml
            refresh: true
          - group: DEFAULT_GROUP
            data-id: spring-cloud-nacos-redis.yaml
            refresh: true
```

## 刷新配置

1. ```@RefreshScope``` + ```@Value```
2. ```@RefreshScope``` + ```@Component``` + ```@ConfigurationProperties```

# 注册中心

