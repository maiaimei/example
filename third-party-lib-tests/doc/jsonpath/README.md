# JsonPath Guide

JsonPath，是一种JSON数据结构节点定位和导航表达式语言。可以使用JsonPath来提取特定的JSON字段、过滤数据、执行计算等操作。

源码：https://github.com/json-path/JsonPath

文档：https://github.com/json-path/JsonPath#getting-started

使用JsonPath，如果使用Maven，在pom.xml中添加依赖，如下所示：

```xml

<dependency>
  <groupId>com.jayway.jsonpath</groupId>
  <artifactId>json-path</artifactId>
  <version>${json-path.version}</version>
</dependency>
```

## 基本语法

[https://raw.githubusercontent.com/json-path/JsonPath/8a0d2fd594b4a1baab71132cf5ef74889b9976e5/README.md](https://raw.githubusercontent.com/json-path/JsonPath/8a0d2fd594b4a1baab71132cf5ef74889b9976e5/README.md)

JSONPath 的语法非常简洁和直观，它由一系列操作符和表达式组成，用于指定要查询的路径和条件。

### Operators - 操作符

以下是 JSONPath 的一些基本语法规则：

- **点符号表示法（**.**）**：使用点表示法可以访问对象的属性。例如，`$.store.book` 表示访问 JSON 结构中的 `store` 对象下的 `book` 属性。
- **递归下降（**..**）**：递归下降运算符允许在 JSON 结构中向下递归搜索。例如，`$..book` 表示访问 JSON 结构中的所有 `book` 属性，无论它们位于何处。
- **通配符（*****）**：通配符用于匹配任意元素。例如，`$.store.*` 表示访问 `store` 对象下的所有属性。
- **方括号表示法（**[]**）**：方括号表示法用于访问数组元素或使用条件进行过滤。例如，`$..book[2]` 表示访问所有 `book` 数组中的第三个元素。
- **过滤器表达式（**[?]**）**：过滤器表达式允许根据特定条件对结果进行筛选。例如，`$.store.book[?(@.price < 10)]` 表示查找价格低于 10 的书籍。

| Operator                | Description                                                                                                                                         |
|:------------------------|:----------------------------------------------------------------------------------------------------------------------------------------------------|
| `$`                     | The root element to query. This starts all path expressions.<br />根对象                                                                               |
| `@`                     | The current node being processed by a filter predicate.<br />当前节点                                                                                   |
| `*`                     | Wildcard. Available anywhere a name or numeric are required.<br />通配符，表示所有对象，元素或属性                                                                  |
| `.name`                 | Dot-notated child<br />子元素、直接孩子节点。用于访问对象的属性。例如，$.name 或 $[name] 都可以访问根对象中的 ‘name’ 属性。                                                               |
| `..name`                | Deep scan. Available anywhere a name is required.<br />深度扫描，用于查找所有级别的属性。                                                                            |
| `['name1' (, 'name2')]` | Bracket-notated child or children<br />多个属性访问。<br />[]：在属性名或数组索引位置使用，表示选择所有元素。例如，$.students[*].name 将选择所有学生的名字。                                     |
| `[number1 (, number2)]` | Array index or indexes<br />数组索引，指定索引                                                                                                               |
| `[start:end]`           | Array slice operator<br />数组切片，范围访问<br />使用切片语法（如`[start:end:step]`）来选择数组中的特定元素范围。                                                                  |
| `[?(expression)]`       | Filter expression. Expression must evaluate to a boolean value.<br />条件过滤，必须为Boolean类型。<br />?()：应用一个过滤表达式来过滤数组中的元素。例如，$?(@.age>18) 将选择所有年龄大于18的对象。 |

### Filter Operators - 条件操作符

Filters are logical expressions used to filter arrays.

A typical filter would be `[?(@.age > 18)]` where `@` represents the current item being processed.

More complex filters can be created with logical operators `&&` and `||`.

String literals must be enclosed by single or double quotes (`[?(@.color == 'blue')]` or `[?(@.color == "blue")]`).

| Operator | Description                                                        |
|:---------|:-------------------------------------------------------------------|
| ==       | left is equal to right (note that 1 is not equal to '1')           |
| !=       | left is not equal to right                                         |
| <        | left is less than right                                            |
| <=       | left is less or equal to right                                     |
| >        | left is greater than right                                         |
| >=       | left is greater than or equal to right                             |
| =~       | left matches regular expression  [?(@.name =~ /foo.*?/i)]          |
| in       | left exists in right [?(@.size in ['S', 'M'])]                     |
| nin      | left does not exists in right                                      |
| subsetof | left is a subset of right [?(@.sizes subsetof ['S', 'M', 'L'])]    |
| anyof    | left has an intersection with right [?(@.sizes anyof ['M', 'L'])]  |
| noneof   | left has no intersection with right [?(@.sizes noneof ['M', 'L'])] |
| size     | size of left (array or string) should match right                  |
| empty    | left (array or string) should be empty                             |

### Functions - 内置函数

Functions can be invoked at the tail end of a path - the input to a function is the output of the path expression.
The function output is dictated by the function itself.

| Function  | Description                                                                          | Output type          |
|:----------|:-------------------------------------------------------------------------------------|:---------------------|
| min()     | Provides the min value of an array of numbers                                        | Double               |
| max()     | Provides the max value of an array of numbers                                        | Double               |
| avg()     | Provides the average value of an array of numbers                                    | Double               |
| stddev()  | Provides the standard deviation value of an array of numbers                         | Double               |
| length()  | Provides the length of an array<br />获取数组或字符串长度                                      | Integer              |
| sum()     | Provides the sum value of an array of numbers                                        | Double               |
| keys()    | Provides the property keys (An alternative for terminal tilde `~`)<br />获取对象所有键      | `Set<E>`             |
| concat(X) | Provides a concatinated version of the path output with a new item                   | like input           |
| append(X) | add an item to the json path output array                                            | like input           |
| first()   | Provides the first item of an array                                                  | Depends on the array |
| last()    | Provides the last item of an array                                                   | Depends on the array |
| index(X)  | Provides the item of an array of index: X, if the X is negative, take from backwards | Depends on the array |

## 应用场景

Java JSONPath 是一个用于处理 JSON 数据的库，它允许你在 JSON 对象中查询和操作数据。JSONPath 适用于包括但不限于以下场景：

- 数据验证：JSONPath 可以用于验证 JSON 数据是否符合预期的格式和结构。例如，你可以使用 JSONPath 检查某个字段是否存在，或者检查某个字段的值是否在允许的范围内。
- 数据绑定：JSONPath 可以用于将 JSON 数据绑定到 Java 对象，这样你可以更方便地访问和处理 JSON 数据。
- 数据转换：JSONPath 可以用于将 JSON 数据转换为其他格式，例如将 JSON 数据转换为 CSV 或 XML。这可以帮助你在不同的数据格式之间进行转换，以便于数据的处理和传输。
- 数据过滤：JSONPath 可以用于过滤 JSON 数据，以便只返回符合特定条件的数据。例如，你可以使用 JSONPath 过滤出某个数组中的所有偶数元素。
- 数据聚合：JSONPath 可以用于对 JSON 数据进行聚合操作，例如计算某个字段的平均值、最大值或最小值。
- 数据操作：JSONPath 可以用于对 JSON 数据进行操作，例如修改某个字段的值、添加新的字段或者删除某个字段。
- 日志分析：对于包含 JSON 格式的日志文件，JSONPath 可以帮助你快速提取和分析关键信息。
- 自动化测试：在自动化测试中，你可以使用 JSONPath 来验证 API 响应中的数据是否符合预期。

## 知识扩展

XPath，XML Path Language，是一种XML数据结构节点定位和导航表达式语言。

OGNL，Object-Graph Navigation Language，是一种对象属性定位和导航表达式语言。
