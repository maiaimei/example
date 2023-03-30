package cn.maiaimei.example;

import cn.maiaimei.example.gateway.ExampleGateway;
import cn.maiaimei.framework.util.SFID;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class ExampleGatewayTest {

    @Autowired
    ExampleGateway exampleGateway;

    @Test
    void method1() {
        exampleGateway.method1("sit", SFID.nextId());
    }
}
