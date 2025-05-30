### @ConditionalOnProperty

```java
@ConditionalOnProperty(
    prefix = "feature",              // 属性名前缀
    name = "email-service",          // 属性名
    havingValue = "true",           // 期望的属性值
    matchIfMissing = false,         // 属性不存在时的默认处理
    value = "",                     // name的别名
    relaxedNames = true             // 是否宽松匹配属性名
)
```

