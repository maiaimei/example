

# 短轮询和长轮询

我们知道HTTP 协议有一个缺陷：通信只能由客户端发起，服务器端无法向某个客户端推送数据。然而，在某些场景下，数据推送是非常必要的功能，为了实现推送技术，所用的技术都是轮询，即：客户端在特定的的时间间隔（如每 1 秒），由浏览器对服务器发出 HTTP 请求，然后由服务器返回最新的数据给客户端的浏览器。

短轮询和长轮询是两种客户端与服务器进行数据交换的技术，主要用于实现如即时消息、实时更新等功能‌。

定义和基本原理

- ‌短轮询‌：客户端定期向服务器发送请求，询问是否有新的数据。服务器在接收到请求后，无论是否有新数据，都会立即响应。如果服务器没有新数据，客户端会在设定的时间间隔后再次发送请求‌12。
- ‌长轮询‌：客户端发送请求到服务器后，服务器会保持这个连接，直到有新数据可以发送，然后才响应请求并关闭连接。客户端在接收到服务器的响应后，立即再次发起新的请求‌12。

优缺点

- ‌短轮询：
  - ‌优点‌：实现简单，兼容性好，适用于任何类型的服务器。
  - ‌缺点‌：效率低下，可能有大量无效请求，服务器负载较高，响应延迟较大‌12。
- ‌长轮询：
  - ‌优点‌：减少了无效请求，降低了服务器负载，响应更及时‌12。
  - ‌缺点‌：实现相对复杂，服务器需要保持连接，可能会消耗更多资源，对服务器的并发连接数有较高要求‌12。

应用场景

- ‌短轮询‌：适用于实时性要求不高的场景，如小型应用或传统Web通信模式‌23。
- ‌长轮询‌：适用于需要提高实时性的应用场景，如即时消息、实时更新等‌

# SSE（Server-Sent Events）

SSE（Server-Sent Events）是一种用于实现服务器主动向客户端推送数据的技术，也被称为“事件流”（Event Stream）。它基于 HTTP 协议，利用了其长连接特性，在客户端与服务器之间建立一条持久化连接，并通过这条连接实现服务器向客户端的实时数据推送。

SSE 的优点和适用场景

- 简单易用: SSE 协议相对于 WebSocket 更简单，实现起来更加轻量级，不需要复杂的握手过程。
- 实时性: SSE 适合需要实时推送数据的场景，如即时通讯、股票市场报价、实时数据监控等。
- 基于标准: SSE 是基于 HTTP 的标准协议，与现有的 Web 技术兼容性良好。

SSE 的实现步骤

- 服务端实现: 使用支持 SSE 的服务器端技术（如Node.js的express框架），在 HTTP 头部添加特定的 Content-Type（text/event-stream）和其他 SSE 相关字段，定期向客户端发送数据。

  ```java
  res.writeHead(200, {
    'Content-Type': 'text/event-stream',
    'Cache-Control': 'no-cache',
    'Connection': 'keep-alive',
  });
  
  res.write('data: Hello\n\n'); // 发送数据给客户端
  ```

- 客户端实现: 使用 JavaScript 创建一个 EventSource 对象，监听从服务器发送的事件。

  ```javascript
  var eventSource = new EventSource('/sse-endpoint');
  
  eventSource.onmessage = function(event) {
    console.log('Received event: ', event.data);
    // 处理接收到的数据
  };
  ```

SSE 的局限性

- 单向通信: SSE 是服务器向客户端的单向通信，客户端无法向服务器端发送数据，因此不适合需要双向通信的应用。
- 兼容性: 虽然现代浏览器普遍支持 SSE，但是在一些旧版本浏览器和移动设备上的支持可能有限。

# WebSockets Tutorial

WebSocket 是一种在浏览器和服务器之间建立双向通信通道的协议。它允许服务器在任意时刻发送消息给浏览器，而不需要浏览器先发起请求。

WebSocket协议是基于TCP的一种网络协议，它实现了浏览器与服务器全双工（Full-duplex）通信。它允许服务端主动向客户端推送数据，这使得客户端和服务器之间的数据交换变得更加简单高效。在WebSocket API中，浏览器和服务器只需要完成一次握手，两者之间就可以创建持久性的连接，并进行双向数据传输。

## Developing Your First WebSocket Application

Here’s a simple Spring Boot project that demonstrates the use of both `spring-boot-starter-web` and `spring-boot-starter-websocket`. This project includes a REST API and a WebSocket endpoint.

---

### **1. Project Setup**
Add the following dependencies to your `pom.xml` (for Maven):

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter WebSocket -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>
</dependencies>
```

---

### **2. Application Structure**
The project will have:
- A REST controller to handle HTTP requests.
- A WebSocket configuration and endpoint for real-time communication.

---

### **3. Code Implementation**

#### **Main Application Class**
```java
package com.example.websocketdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WebsocketDemoApplication {
    public static void main(String[] args) {
        SpringApplication.run(WebsocketDemoApplication.class, args);
    }
}
```

---

#### **WebSocket Configuration**
Configure WebSocket support in the application.

```java
package com.example.websocketdemo.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;
import com.example.websocketdemo.handler.MyWebSocketHandler;

@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new MyWebSocketHandler(), "/ws").setAllowedOrigins("*");
    }
}
```

---

#### **WebSocket Handler**
Handle WebSocket messages.

```java
package com.example.websocketdemo.handler;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class MyWebSocketHandler extends TextWebSocketHandler {

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String clientMessage = message.getPayload();
        System.out.println("Received message: " + clientMessage);

        // Echo the message back to the client
        session.sendMessage(new TextMessage("Server response: " + clientMessage));
    }
}
```

---

#### **REST Controller**
Create a simple REST API.

```java
package com.example.websocketdemo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class MyRestController {

    @GetMapping("/hello")
    public String sayHello() {
        return "Hello from REST API!";
    }
}
```

---

### **4. Running the Application**
1. Start the Spring Boot application.
2. Access the REST API:
   - Open a browser or use a tool like `curl` or Postman to hit `http://localhost:8080/api/hello`.
   - You should see the response: `"Hello from REST API!"`.

3. Test the WebSocket:
   - Use a WebSocket client (e.g., [websocket.org](https://www.websocket.org/echo.html)) to connect to `ws://localhost:8080/ws`.
   - Send a message, and the server will echo it back.

---

### **5. Example WebSocket Interaction**
- **Client sends**: `Hello, WebSocket!`
- **Server responds**: `Server response: Hello, WebSocket!`

---

This project demonstrates how to use both REST and WebSocket endpoints in a Spring Boot application. You can extend it further based on your requirements.

## Enhance WebSocket Application

在上述代码的基础上，我们可以通过添加 `jakarta.websocket-api` 依赖并使用 `@ServerEndpoint` 注解来实现基于 Jakarta WebSocket 的 WebSocket 服务端。以下是完整的实现步骤：

---

### **1. 添加依赖**
在 `pom.xml` 中添加 `jakarta.websocket-api` 和 `spring-boot-starter-tomcat`（默认支持 WebSocket 的容器）依赖：

```xml
<dependencies>
    <!-- Spring Boot Starter Web -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>

    <!-- Spring Boot Starter WebSocket -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-websocket</artifactId>
    </dependency>

    <!-- Jakarta WebSocket API -->
    <dependency>
        <groupId>jakarta.websocket</groupId>
        <artifactId>jakarta.websocket-api</artifactId>
        <version>2.1.1</version>
    </dependency>
</dependencies>
```

---

### **2. 配置 WebSocket 服务端**
使用 `@ServerEndpoint` 注解创建一个 WebSocket 服务端。

#### **WebSocket Server Endpoint**
```java
package com.example.websocketdemo.endpoint;

import jakarta.websocket.OnClose;
import jakarta.websocket.OnError;
import jakarta.websocket.OnMessage;
import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.concurrent.CopyOnWriteArraySet;

@ServerEndpoint("/ws-jakarta")
@Component
public class JakartaWebSocketEndpoint {

    // Store all active WebSocket sessions
    private static final CopyOnWriteArraySet<JakartaWebSocketEndpoint> webSocketSet = new CopyOnWriteArraySet<>();
    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        webSocketSet.add(this);
        System.out.println("New connection opened: " + session.getId());
        sendMessage("Welcome! Your session ID is " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) {
        System.out.println("Received message from session " + session.getId() + ": " + message);

        // Broadcast the message to all connected clients
        for (JakartaWebSocketEndpoint endpoint : webSocketSet) {
            endpoint.sendMessage("Broadcast from " + session.getId() + ": " + message);
        }
    }

    @OnClose
    public void onClose(Session session) {
        webSocketSet.remove(this);
        System.out.println("Connection closed: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("Error in session " + session.getId() + ": " + throwable.getMessage());
    }

    private void sendMessage(String message) {
        try {
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            System.err.println("Error sending message: " + e.getMessage());
        }
    }
}
```

### **3. 配置 WebSocket 容器**

Spring Boot 默认使用 Tomcat 作为嵌入式容器，支持 Jakarta WebSocket。我们需要将 `@ServerEndpoint` 注册到 Spring 容器中。

#### **WebSocket Config**

```java
package com.example.websocketdemo.config;

import jakarta.websocket.server.ServerEndpointConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

@Configuration
public class WebSocketConfig {

    /**
     * Automatically register @ServerEndpoint classes as WebSocket endpoints.
     */
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
```

### **4. 测试 WebSocket**

1. 启动 Spring Boot 应用程序。
2. 使用 WebSocket 客户端（如 [websocket.org](vscode-file://vscode-app/d:/Microsoft VS Code/resources/app/out/vs/code/electron-sandbox/workbench/workbench.html) 或 Postman）连接到 `ws://localhost:8080/ws-jakarta`。
3. 发送消息，观察服务器的广播行为。

### 5. **关键点**

- `@ServerEndpoint` 是 Jakarta WebSocket 的核心注解，用于定义 WebSocket 服务端。
- 标注有 `@ServerEndpoint` 注解的类需要注册到Spring容器中，否则客户端无法连接。在该类中注入其他Bean时，如果Bean为null，可以利用`SpringBeanFactory`类的工具类来获取相应的Bean。
- `ServerEndpointExporter` 是 Spring Boot 提供的工具，用于自动探测并将标注有 `@ServerEndpoint` 注解的Bean注册到 WebSocket 容器中。
- 使用 `CopyOnWriteArraySet` 管理所有 WebSocket 会话，方便实现广播功能。
- 每个客户端连接都对应一个标注有`@ServerEndpoint` 注解的类的新对象

## WebSocket的事件

我们知道HTTP协议使用http和https的统一资源标志符。WebSocket与HTTP类似，使用的是 ws 或 wss（类似于 HTTPS），其中 wss 表示在 TLS 之上的Websocket。例如：

```
ws://example.com/wsapi
wss://secure.example.com/
```

WebSocket 使用和 HTTP 相同的 TCP 端口，可以绕过大多数防火墙的限制。默认情况下， WebSocket 协议使用80 端口；运行在 TLS 之上时，默认使用 443 端口。

WebSocket 只是在 Socket 协议的基础上，非常轻的一层封装。在WebSocket API中定义了open、close、error、message等几个基本事件，这就使得WebSocket使用起来非常简单。 下面是在WebSocket API定义的事件：

| 事件    | 事件处理程序      | 描述                       |
| ------- | ----------------- | -------------------------- |
| open    | Sokcket onopen    | 连接建立时触发             |
| message | Sokcket onmessage | 客户端接收服务端数据时触发 |
| error   | Sokcket onerror   | 通讯发生错误时触发         |
| close   | Sokcket onclose   | 连接关闭时触发             |

## jakarta.websocket-api

Jakarta WebSocket API is a platform-independent websocket protocol API to build bidirectional communications over the web. It is included in the Jakarta EE platform. Jakarta WebSocket defines an API for Server and Client Endpoints for the WebSocket protocol (RFC6455).

[https://jakartaee.github.io/websocket/](https://jakartaee.github.io/websocket/)

[https://jakarta.ee/specifications/websocket/](https://jakarta.ee/specifications/websocket/)

[https://jakarta.ee/specifications/websocket/2.0/apidocs/](https://jakarta.ee/specifications/websocket/2.0/apidocs/)

[https://jakarta.ee/specifications/websocket/2.1/jakarta-websocket-spec-2.1](https://jakarta.ee/specifications/websocket/2.1/jakarta-websocket-spec-2.1)

### Annotations

#### @OnOpen

```java
@Retention(value=RUNTIME)
@Target(value=METHOD)
public @interface OnOpen
```

This method level annotation can be used to decorate a Java method that wishes to be called when a new web socket session is open.

The method may only take the following parameters:-

- optional [`Session`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/session) parameter
- optional [`EndpointConfig`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/endpointconfig) parameter
- Zero to n String parameters annotated with the `jakarta.websocket.server.PathParam` annotation.

The parameters may appear in any order.

#### @OnClose

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnClose
```

This method level annotation can be used to decorate a Java method that wishes to be called when a web socket session is closing.

The method may only take the following parameters:-

- optional [`Session`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/session) parameter
- optional [`CloseReason`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/closereason) parameter
- Zero to n String parameters annotated with the `jakarta.websocket.server.PathParam` annotation.

The parameters may appear in any order. See [`Endpoint.onClose(jakarta.websocket.Session, jakarta.websocket.CloseReason)`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/endpoint#onClose-jakarta.websocket.Session-jakarta.websocket.CloseReason-) for more details on how the session parameter may be used during method calls annotated with this annotation.

#### @OnError

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnError
```

This method level annotation can be used to decorate a Java method that wishes to be called in order to handle errors. See [`Endpoint.onError(jakarta.websocket.Session, java.lang.Throwable)`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/endpoint#onError-jakarta.websocket.Session-java.lang.Throwable-) for a description of the different categories of error.

The method may only take the following parameters:-

- optional [`Session`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/session) parameter
- a [`Throwable`](https://docs.oracle.com/javase/8/docs/api/java/lang/Throwable.html?is-external=true) parameter
- Zero to n String parameters annotated with the `jakarta.websocket.server.PathParam` annotation

The parameters may appear in any order.

#### @OnMessage

```java
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface OnMessage
```

This method level annotation can be used to make a Java method receive incoming web socket messages. Each websocket endpoint may only have one message handling method for each of the native websocket message formats: text, binary and pong. Methods using this annotation are allowed to have parameters of types described below, otherwise the container will generate an error at deployment time.

The allowed parameters are:

1. Exactly one of any of the following choices
   - if the method is handling text messages:
     - [`String`](https://docs.oracle.com/javase/8/docs/api/java/lang/String.html?is-external=true) to receive the whole message
     - Java primitive or class equivalent to receive the whole message converted to that type
     - String and boolean pair to receive the message in parts
     - [`Reader`](https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html?is-external=true) to receive the whole message as a blocking stream
     - any object parameter for which the endpoint has a text decoder ([`Decoder.Text`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/decoder.text) or [`Decoder.TextStream`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/decoder.textstream)).
   - if the method is handling binary messages:
     - byte[] or [`ByteBuffer`](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html?is-external=true) to receive the whole message
     - byte[] and boolean pair, or [`ByteBuffer`](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html?is-external=true) and boolean pair to receive the message in parts
     - [`InputStream`](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html?is-external=true) to receive the whole message as a blocking stream
     - any object parameter for which the endpoint has a binary decoder ([`Decoder.Binary`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/decoder.binary) or [`Decoder.BinaryStream`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/decoder.binarystream)).
   - if the method is handling pong messages:
     - [`PongMessage`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/pongmessage) for handling pong messages
2. and Zero to n String or Java primitive parameters annotated with the `jakarta.websocket.server.PathParam` annotation for server endpoints.
3. and an optional [`Session`](https://jakarta.ee/specifications/websocket/2.0/apidocs/jakarta/websocket/session) parameter

The parameters may be listed in any order.

The method may have a non-void return type, in which case the web socket runtime must interpret this as a web socket message to return to the peer. The allowed data types for this return type, other than void, are String, ByteBuffer, byte[], any Java primitive or class equivalent, and anything for which there is an encoder. If the method uses a Java primitive as a return value, the implementation must construct the text message to send using the standard Java string representation of the Java primitive unless there developer provided encoder for the type configured for this endpoint, in which case that encoder must be used. If the method uses a class equivalent of a Java primitive as a return value, the implementation must construct the text message from the Java primitive equivalent as described above.

Developers should note that if developer closes the session during the invocation of a method with a return type, the method will complete but the return value will not be delivered to the remote endpoint. The send failure will be passed back into the endpoint's error handling method.

For example:

```java
@OnMessage
public void processGreeting(String message, Session session) {
	System.out.println("Greeting received:" + message);
}
```

For example:

```java
@OnMessage
public void processUpload(byte[] b, boolean last, Session session) {
    // process partial data here, which check on last to see if these is more on the way
}
```

Developers should not continue to reference message objects of type [`Reader`](https://docs.oracle.com/javase/8/docs/api/java/io/Reader.html?is-external=true), [`ByteBuffer`](https://docs.oracle.com/javase/8/docs/api/java/nio/ByteBuffer.html?is-external=true) or [`InputStream`](https://docs.oracle.com/javase/8/docs/api/java/io/InputStream.html?is-external=true) after the annotated method has completed, since they may be recycled by the implementation.

## Reference

[https://spring.io/projects/spring-boot#learn](https://spring.io/projects/spring-boot#learn)

[https://docs.spring.io/spring-boot/tutorial/first-application/index.html](https://docs.spring.io/spring-boot/tutorial/first-application/index.html)

[https://docs.spring.io/spring-boot/reference/messaging/websockets.html#page-title](https://docs.spring.io/spring-boot/reference/messaging/websockets.html#page-title)

[https://spring.io/guides/gs/messaging-stomp-websocket](https://spring.io/guides/gs/messaging-stomp-websocket)

[https://www.bilibili.com/video/BV1u1421t7uS?spm_id_from=333.788.videopod.sections&vd_source=80612925dae54b29d86f65198f1081f4](https://www.bilibili.com/video/BV1u1421t7uS?spm_id_from=333.788.videopod.sections&vd_source=80612925dae54b29d86f65198f1081f4)

[https://developer.aliyun.com/article/1630812](https://developer.aliyun.com/article/1630812)