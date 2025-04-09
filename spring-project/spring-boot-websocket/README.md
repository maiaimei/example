

# WebSockets Tutorial

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

    <!-- Tomcat WebSocket Implementation -->
    <dependency>
        <groupId>org.apache.tomcat</groupId>
        <artifactId>tomcat-websocket</artifactId>
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

## WebSocket client

[https://www.wetools.com/websocket](https://www.wetools.com/websocket)

## Reference

[https://spring.io/projects/spring-boot#learn](https://spring.io/projects/spring-boot#learn)

[https://docs.spring.io/spring-boot/tutorial/first-application/index.html](https://docs.spring.io/spring-boot/tutorial/first-application/index.html)

[https://docs.spring.io/spring-boot/reference/messaging/websockets.html#page-title](https://docs.spring.io/spring-boot/reference/messaging/websockets.html#page-title)

[https://spring.io/guides/gs/messaging-stomp-websocket](https://spring.io/guides/gs/messaging-stomp-websocket)

[https://www.bilibili.com/video/BV1u1421t7uS?spm_id_from=333.788.videopod.sections&vd_source=80612925dae54b29d86f65198f1081f4](https://www.bilibili.com/video/BV1u1421t7uS?spm_id_from=333.788.videopod.sections&vd_source=80612925dae54b29d86f65198f1081f4)