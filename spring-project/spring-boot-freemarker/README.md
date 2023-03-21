[https://freemarker.apache.org/](https://freemarker.apache.org/)

数字原样输出，变量名后加 “?c”

```
${id?c}
${amount?c}
```

默认值，变量名后加 “!默认值”

```
${enabled!"false"}
```

