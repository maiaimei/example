# Spring Expression Language Guide

## Overview

The Spring Expression Language (SpEL) is a powerful expression language that supports querying and manipulating an object graph at runtime.

## Operators

There are several operators available in the language:

| Type         | Operators                                    |
| :----------- | :------------------------------------------- |
| Mathematical | +, -, *, /, %, ^, div, mod                   |
| Relational   | <, >, ==, !=, <=, >=, lt, gt, eq, ne, le, ge |
| Logical      | and, or, not, &&, \|\|, !                    |
| Conditional  | ?:                                           |
| Regex        | matches                                      |

## The #this and #root variables

The variable #this is always defined and refers to the current evaluation object (against which unqualified references are resolved). The variable #root is always defined and refers to the root context object. Although #this may vary as components of an expression are evaluated, #root always refers to the root.

## Collection Selection

Selection is a powerful expression language feature that allows you to transform some source collection into another by selecting from its entries.

Selection uses the syntax `?[selectionExpression]`. This will filter the collection and return a new collection containing a subset of the original elements. 

Selection is possible upon both lists and maps.

In addition to returning all the selected elements, it is possible to retrieve just the first or the last value. To obtain the first entry matching the selection the syntax is `^[...]` whilst to obtain the last matching selection the syntax is `$[...]`.

## Reference

[https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html](https://docs.spring.io/spring-framework/docs/3.2.x/spring-framework-reference/html/expressions.html)
