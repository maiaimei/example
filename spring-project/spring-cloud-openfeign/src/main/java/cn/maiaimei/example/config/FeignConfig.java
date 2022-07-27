package cn.maiaimei.example.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = "cn.maiaimei.example.client")
public class FeignConfig {
//    @Resource
//    private SimpleDiscoveryProperties simpleDiscoveryProperties;
//
//    @SneakyThrows
//    @Bean
//    public DiscoveryClient discoveryClient() {
//        ObjectMapper objectMapper = new ObjectMapper();
//        log.info("{}", objectMapper.writeValueAsString(simpleDiscoveryProperties));
//        return new SimpleDiscoveryClient(simpleDiscoveryProperties);
//    }
}