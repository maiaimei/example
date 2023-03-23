# freemarker

[https://freemarker.apache.org/](https://freemarker.apache.org/)

[http://doc.foofun.cn/FreeMarker_2.3.23_Manual_zh_CN/index.html](http://doc.foofun.cn/FreeMarker_2.3.23_Manual_zh_CN/index.html)

## Interpolations

```
${expression}
```

## Built-in Reference

[https://freemarker.apache.org/docs/ref_builtins.html](https://freemarker.apache.org/docs/ref_builtins.html)

## Directive Reference

[https://freemarker.apache.org/docs/ref_directives.html](https://freemarker.apache.org/docs/ref_directives.html)

### if, else, elseif

```xml
<#if condition>
  ...
<#elseif condition2>
  ...
<#elseif condition3>
  ...
...
<#else>
  ...
</#if>
```

### switch, case, default, break

```xml
<#switch value>
  <#case refValue1>
    ...
    <#break>
  <#case refValue2>
    ...
    <#break>
  ...
  <#case refValueN>
    ...
    <#break>
  <#default>
    ...
</#switch>
```

### list, else, items, sep, break, continue

```xml
<#list sequence as item>
    Part repeated for each item
</#list>
```

```xml
<#list sequence as item>
    Part repeated for each item
<#else>
    Part executed when there are 0 items
</#list>
```

```xml
<#list sequence>
    Part executed once if we have more than 0 items
    <#items as item>
        Part repeated for each item
    </#items>
    Part executed once if we have more than 0 items
<#else>
    Part executed when there are 0 items
</#list>
```

`sep` is used when you have to display something between each item (but not before the first item or after the last item)

```xml
<#list sequence>
    Part executed once if we have more than 0 items
    <#items as item>
        Part repeated for each item
        <#sep>, </#sep>
    </#items>
    Part executed once if we have more than 0 items
<#else>
    Part executed when there are 0 items
</#list>
```

### include

```xml
<#include path>
or
<#include path options>
```

```xml
<#assign me = "Juila Smith">
<h1>Some test</h1>
<p>Yeah.
<hr>
<#include "/common/copyright.ftl">
```

## Expressions

[https://freemarker.apache.org/docs/dgui_template_exp.html](https://freemarker.apache.org/docs/dgui_template_exp.html)

### ${someValue?c}

By default **`${someValue}`** outputs numbers, date/time/date-time and boolean values in a format that targets normal users ("humans"). You have various settings to specify how that format looks, like **number_format, date_format, time_format, datetime_format, boolan_format**. The output also often depends on the **locale** setting (i.e., on the language, and country of the user). So **3000000** is possibly printed as **3,000,000** (i.e., with grouping separators), or **3.14** is possibly printed as **3,14** (i.e., with a different decimal separator).

At some places you need to output values that will be read (parsed) by some program, in which case always use the c built-in, as in **`${someValue?c}`** (the "c" stands for Computer). Then the formatting depends on the c_format setting, which usually refers to a computer language, like "JSON". The output of ?c is not influenced by locale, number_format, etc.

The c built-in will format string values to string literals. Like if the c_format setting is "JSON", then {"fullName": ${fullName?c}} will output something like {"fullName": "John Doe"}, where the quotation marks (and \ escaping if needed) were added by ?c.

```
${someValue?c}  # the "c" stands for Computer
${someValue?cn} # the "n" stands for Nullable
```

### Default value operator
Synopsis: `unsafe_expr!default_expr` or `unsafe_expr!` or `(unsafe_expr)!default_expr` or `(unsafe_expr)!`

```
${someValue!}
${someValue!defaultValue}
${enabled!"false"}
${user!"null"}
${user!}
${user.username!}
${(user.username)!}
```

### Missing value test operator

Synopsis: `unsafe_expr??` or `(unsafe_expr)??`

This operator tells if a value is missing or not. Depending on that, the result is either true or false.

You can ask whether a variable isn't missing by putting **??** after its name. Combining this with the already introduced if directive you can skip the whole greeting if the user variable is missing:

```xml
<#if user??><h1>Welcome ${user}!</h1></#if>
```
