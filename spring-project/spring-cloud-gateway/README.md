[https://spring.io/projects/spring-cloud#overview](https://spring.io/projects/spring-cloud#overview)

[https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/](https://docs.spring.io/spring-cloud-gateway/docs/current/reference/html/)

**Route**: The basic building block of the gateway. It is defined by an ID, a destination URI, a collection of predicates, and a collection of filters. A route is matched if the aggregate predicate is true.

**Predicate**: This is a [Java 8 Function Predicate](https://docs.oracle.com/javase/8/docs/api/java/util/function/Predicate.html). The input type is a [Spring Framework `ServerWebExchange`](https://docs.spring.io/spring/docs/5.0.x/javadoc-api/org/springframework/web/server/ServerWebExchange.html). This lets you match on anything from the HTTP request, such as headers or parameters.

**Filter**: These are instances of [`GatewayFilter`](https://github.com/spring-cloud/spring-cloud-gateway/tree/main/spring-cloud-gateway-server/src/main/java/org/springframework/cloud/gateway/filter/GatewayFilter.java) that have been constructed with a specific factory. Here, you can modify requests and responses before or after sending the downstream request.

```jade
org.springframework.cloud.gateway.handler.predicate.GatewayPredicate
org.springframework.cloud.gateway.filter.GatewayFilter
```

