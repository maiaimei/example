package cn.maiaimei.samples.channeladapter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ImportResource;

@SpringBootApplication
//@Import(ChannelAdapterConfig.class)
@ImportResource(locations = "classpath:beans/channel-adapter.xml")
public class ChannelAdapterApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChannelAdapterApplication.class, args);
    }
}
