Spring Expression Language（简称 SpEL）是一种功能强大的表达式语言，支持 **运行时 查询和操作对象图** 。SpEL 可以独立于 Spring 容器使用，但只是被当作成简单的表达式语言来使用。在未对用户的输入做严格的检查，以及错误使用 Spring 表达式语言时，就有可能产生表达式注入漏洞。

**SpEL执行过程： 字符串 -> 语法分析 -> 生成表达式对象 -> （添加执行上下文） -> 执行此表达式对象 -> 返回结果**

1. **表达式（“干什么”）**：SpEL 的核心，所以表达式语言都是围绕表达式进行的

2. **解析器（“谁来干”）**：SpelExpressionParser用于将字符串表达式解析为表达式对象

3. **上下文（“在哪干”）**：表达式对象执行的环境，该环境可能定义变量、定义自定义函数、提供类型转换等等。默认是StandardEvaluationContext，这个是可选的。
   * SimpleEvaluationContext：不包含类相关的危险操作，比较安全
   
   * StandardEvaluationContext：包含所有功能，存在风险
   
4. **root根对象及活动上下文对象（“对谁干”）**：root根对象是默认的活动上下文对象，活动上下文对象表示了当前表达式操作的对象

5. **返回结果**：使用表达式对象求值，例如getValue

[Spring Expression Language (SpEL)](https://docs.spring.io/spring-framework/reference/core/expressions.html)

[Language Reference](https://docs.spring.io/spring-framework/reference/core/expressions/language-ref.html)

[Variables](https://docs.spring.io/spring-framework/reference/core/expressions/language-ref/variables.html)
