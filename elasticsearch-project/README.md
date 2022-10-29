[https://www.elastic.co/cn/elasticsearch/](https://www.elastic.co/cn/elasticsearch/)

[https://www.elastic.co/cn/downloads/elasticsearch](https://www.elastic.co/cn/downloads/elasticsearch)

[https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)

[https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html](https://www.elastic.co/guide/en/elasticsearch/reference/current/rest-apis.html)

[【尚硅谷】ElasticSearch教程入门到精通（基于ELK技术栈elasticsearch 7.x+8.x新特性）](https://www.bilibili.com/video/BV1hh411D7sb)

# Windows安装ElasticSearch

1. 解压ES

2. 修改config/elasticsearch.yml

   ```yaml
   # Enable security features
   xpack.security.enabled: false
   ```

3. 运行bin/elasticsearch.bat

4. 在浏览器访问http://localhost:9200

   如果看到以下信息，则表示ES服务启动成功

   ```json
   {
     "name" : "DESKTOP-EV3L4B5",
     "cluster_name" : "elasticsearch",
     "cluster_uuid" : "8eML4bMwRxqXSZ5bhkAU-A",
     "version" : {
       "number" : "8.4.3",
       "build_flavor" : "default",
       "build_type" : "zip",
       "build_hash" : "42f05b9372a9a4a470db3b52817899b99a76ee73",
       "build_date" : "2022-10-04T07:17:24.662462378Z",
       "build_snapshot" : false,
       "lucene_version" : "9.3.0",
       "minimum_wire_compatibility_version" : "7.17.0",
       "minimum_index_compatibility_version" : "7.0.0"
     },
     "tagline" : "You Know, for Search"
   }
   ```

   