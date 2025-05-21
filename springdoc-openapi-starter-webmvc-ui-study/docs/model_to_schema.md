springdoc-openapi 中 Model 转换为 OpenAPI Schema 的核心原理和关键类:

核心原理:

1. 模型扫描: 通过反射机制扫描 Java Model 类的属性和注解
2. 类型转换: 将 Java 类型映射为 OpenAPI Schema 类型
3. 注解解析: 处理诸如 @Schema、@JsonProperty 等注解来补充 Schema 定义
4. Schema 生成: 根据解析的信息构建 OpenAPI Schema 对象

关键类和其作用:

1. `ModelConverters`

```java
public class ModelConverters {
    private static final ModelConverters INSTANCE = new ModelConverters();
    private final List<ModelConverter> converters;
    
    // 负责协调不同的转换器进行模型转换
    public Map<String, Schema> read(Type type) {
        // 遍历所有转换器尝试转换模型
    }
}
```

2.  `PrimitiveType`

```java
public class PrimitiveType {
    // 定义基础类型到 OpenAPI 类型的映射
    private static final Map<String, PrimitiveType> primitiveMap;
    private final Class<?> keyClass;
    private final String commonName;
    private final Schema schema;
}
```

3.  `ModelResolver`

```java
public class ModelResolver implements ModelConverter {
    // 核心转换逻辑
    public Schema resolve(AnnotatedType type, ModelConverterContext context, Iterator<ModelConverter> next) {
        // 1. 处理基本类型
        // 2. 处理集合类型
        // 3. 处理复杂对象
        // 4. 处理注解
    }
}
```

4.  `PropertyBuilder`

```java
public class PropertyBuilder {
    // 构建属性的 Schema
    public static Schema toSchema(AnnotatedType type) {
        // 处理属性类型
        // 处理属性注解
        // 设置属性约束
    }
}
```

5. `AnnotationsHelper`

```java
public class AnnotationsHelper {
    // 处理各种注解
    public static void applyAnnotations(Schema schema, List<Annotation> annotations) {
        // 处理 @Schema
        // 处理 validation 注解
        // 处理 Jackson 注解
    }
}
```

转换流程示例:

```java
// 1. 假设有一个 User 模型
@Schema(description = "用户模型")
public class User {
    @Schema(description = "用户ID")
    private Long id;
    
    @NotNull
    @Schema(description = "用户名")
    private String username;
}

// 2. 转换过程
ModelConverters converters = ModelConverters.getInstance();
Map<String, Schema> schemas = converters.read(User.class);
schemas.forEach((key, schema) -> {
    System.out.println("Schema for " + key + ":");
    System.out.println(Json.pretty(schema));
    System.out.println("-------------------");
});

// 3. 生成的 Schema 大致结构
{
  "User": {
    "type": "object",
    "description": "用户模型",
    "properties": {
      "id": {
        "type": "integer",
        "format": "int64",
        "description": "用户ID"
      },
      "username": {
        "type": "string",
        "description": "用户名"
      }
    },
    "required": ["username"]
  }
}
```

关键处理步骤:

1. 类型解析:

- 基本类型直接映射到 OpenAPI 类型
- 复杂类型递归解析
- 集合类型特殊处理

2. 注解处理:

- @Schema 注解处理描述信息
- 验证注解处理必填项和约束
- Jackson 注解处理序列化规则

3. 特殊处理:

- 循环引用检测
- 泛型类型处理
- 继承关系处理

4. Schema 缓存:

- 已解析的 Schema 会被缓存
- 避免重复解析相同类型

这个转换机制让我们可以通过简单的 Java 类定义自动生成符合 OpenAPI 规范的 Schema 定义，大大简化了 API 文档的维护工作。