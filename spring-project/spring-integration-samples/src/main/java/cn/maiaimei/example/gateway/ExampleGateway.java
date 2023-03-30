package cn.maiaimei.example.gateway;

import org.springframework.integration.annotation.Gateway;
import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.messaging.handler.annotation.Header;

@MessagingGateway
public interface ExampleGateway {

    @Gateway(requestChannel = "exampleGatewayMethod1In", replyChannel = "exampleGatewayMethod1Out", payloadExpression = "new java.util.Date()")
    String method1(@Header("env") String env, @Header("pid") Long id);
}
