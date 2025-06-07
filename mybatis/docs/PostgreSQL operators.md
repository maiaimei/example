Here is a list of commonly used PostgreSQL operators categorized by their functionality:

### Comparison Operators
- `=` (Equal)
- `<>` or `!=` (Not Equal)
- `<` (Less Than)
- `>` (Greater Than)
- `<=` (Less Than or Equal)
- `>=` (Greater Than or Equal)

### Logical Operators
- `AND`
- `OR`
- `NOT`

### Arithmetic Operators
- `+` (Addition)
- `-` (Subtraction)
- `*` (Multiplication)
- `/` (Division)
- `%` (Modulo)

### String Operators
- `||` (Concatenation)
- `ILIKE` (Case-insensitive LIKE)
- `LIKE` (Pattern Matching)
- `SIMILAR TO` (Regex-like Pattern Matching)
- `~` (Regex Match)
- `~*` (Case-insensitive Regex Match)
- `!~` (Regex Not Match)
- `!~*` (Case-insensitive Regex Not Match)

### JSON Operators

JSON Operators 适用于 PostgreSQL 的 JSON 或 JSONB 类型。

JSON Operators 主要用于检查 JSON 数据的结构和内容（如键是否存在、JSON 包含关系）。

JSON Operators 的操作符通常作用于 JSON 数据，并且某些操作符（如 ?| 和 ?&）需要结合数组来指定键列表。

- `@>` (JSON Contains)

  检查一个 JSON 是否包含另一个 JSON。

  ```sql
  SELECT * FROM table WHERE json_column @> '{"key": "value"}';
  ```

- `<@` (JSON Contained By)

  检查一个 JSON 是否被另一个 JSON 包含。

  ```sql
  SELECT * FROM table WHERE json_column <@ '{"key": "value", "key2": "value2"}';
  ```

- `?` (JSON Key Exists)

  检查 JSON 是否包含指定的键。

  ```sql
  SELECT * FROM table WHERE json_column ? 'key';
  ```

- `?|` (JSON Any Key Exists)

  检查 JSON 是否包含指定键列表中的任意一个键。

  ```sql
  SELECT * FROM table WHERE json_column ?| ARRAY['key1', 'key2'];
  ```

- `?&` (JSON All Keys Exist)

  检查 JSON 是否包含指定键列表中的所有键。

  ```sql
  SELECT * FROM table WHERE json_column ?& ARRAY['key1', 'key2'];
  ```

### Array Operators

Array Operators 适用于 PostgreSQL 的数组类型（ARRAY）。

Array Operators 主要用于检查数组之间的关系（如包含、重叠）。

Array Operators 的操作符通常直接作用于数组数据。

- `=` (Equality)

- `<>` (Inequality)

- `@>` (Array Contains)

  ```sql
  SELECT * FROM table WHERE array_column @> ARRAY[1, 2];
  ```

- `<@` (Array Contained By)

- `&&` (Array Overlap)

### Range Operators
- `@>` (Range Contains Element or Range)
- `<@` (Range Contained By)
- `&&` (Range Overlap)
- `<<` (Range Strictly Left of)
- `>>` (Range Strictly Right of)
- `-|-` (Range Adjacent To)

### Bitwise Operators
- `&` (Bitwise AND)
- `|` (Bitwise OR)
- `#` (Bitwise XOR)
- `~` (Bitwise NOT)
- `<<` (Bitwise Shift Left)
- `>>` (Bitwise Shift Right)

### Geometric Operators
- `=` (Equality)
- `<>` (Inequality)
- `&&` (Overlap)
- `~` (Same as)
- `@` (Contains)
- `~=` (Bounding-box equality)

### Other Operators
- `IS NULL`
- `IS NOT NULL`
- `IS DISTINCT FROM`
- `IS NOT DISTINCT FROM`
- `IN`
- `NOT IN`

This list covers most of the standard PostgreSQL operators. PostgreSQL also allows custom operators to be defined, so the list can be extended based on specific use cases.