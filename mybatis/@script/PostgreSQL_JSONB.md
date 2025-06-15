测试表结构：
```sql
DROP TABLE "JSONB_TEST";
CREATE TABLE "JSONB_TEST" (
    "ID" NUMERIC(22) PRIMARY KEY,
    "STRING_DATA" JSONB,
    "PERSON_DATA" JSONB,
    "PERSON_DATA_LIST" JSONB,
    "MAP_DATA" JSONB,
    "MAP_DATA_LIST" JSONB,
    "ROLE_LIST" JSONB
);
```

示例JSON数据结构：

```json
{
    "name": "John Doe",
    "contact": {
        "address": "New York",
        "phone": "1234567890"
    },
    "tags": ["Important", "Urgent", "Review"],
    "level1": {
        "level2": {
            "value": "test"
        }
    },
    "items": [
        {
            "name": "Product1",
            "price": 100
        },
        {
            "name": "Product2",
            "price": 200
        }
    ]
}
```

jsonb_set函数用法

```sql
-- 基本语法
UPDATE table_name 
SET json_column = jsonb_set(json_column, '{key_path}', '"new_value"'::jsonb)
WHERE condition;

-- 更新顶层key
UPDATE users 
SET data = jsonb_set(data, '{name}', '"John Smith"'::jsonb)
WHERE id = 1;

-- 更新嵌套对象的key
UPDATE users 
SET data = jsonb_set(data, '{contact,address}', '"New York"'::jsonb)
WHERE id = 1;

-- 更新多个顶层key
UPDATE users 
SET data = data || '{"name": "John Smith", "age": 30}'::jsonb
WHERE id = 1;

-- 更新嵌套对象
UPDATE users 
SET data = jsonb_set(
    data,
    '{contact}',
    (data->'contact') || '{"phone": "9876543210"}'::jsonb
)
WHERE id = 1;

-- 向数组添加元素
UPDATE users 
SET data = jsonb_set(
    data,
    '{tags}',
    (data->'tags') || '"new_tag"'::jsonb
)
WHERE id = 1;
```

注意事项：

1. jsonb_set 的第三个参数必须是有效的 JSONB 格式
2. 路径参数使用花括号和逗号分隔
3. 数字值不需要引号，但字符串值需要双引号
4. 更新不存在的路径时，可以通过第四个参数（create_missing）控制是否创建新的key
