JsonPath，是一种JSON数据结构节点定位和导航表达式语言。可以使用JsonPath来提取特定的JSON字段、过滤数据、执行计算等操作。

源码：https://github.com/json-path/JsonPath

文档：https://github.com/json-path/JsonPath#getting-started



基本语法：

| 操作符                    | 描述                        |
| ------------------------- | --------------------------- |
| $                         | 根对象                      |
| @                         | 处理当前节点的表达式        |
| *                         | 通配符，对象的所有属性      |
| ..                        | 提取任意后代节点            |
| .\<name>                  | 提取直接孩子节点            |
| ['\<name>' (, '\<name>')] | 多个属性访问                |
| [\<number> (, \<number>)] | 数组索引，指定索引          |
| [start:end]               | 数组切片，范围访问          |
| [?(\<expression>)]        | 条件过滤，必须为Boolean类型 |

更多语法：[https://raw.githubusercontent.com/json-path/JsonPath/8a0d2fd594b4a1baab71132cf5ef74889b9976e5/README.md](https://raw.githubusercontent.com/json-path/JsonPath/8a0d2fd594b4a1baab71132cf5ef74889b9976e5/README.md)



知识扩展：

XPath，XML Path Language，是一种XML数据结构节点定位和导航表达式语言。

OGNL，Object-Graph Navigation Language，是一种对象属性定位和导航表达式语言。