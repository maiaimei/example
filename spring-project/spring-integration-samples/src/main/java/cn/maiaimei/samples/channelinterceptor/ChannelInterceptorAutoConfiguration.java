package cn.maiaimei.samples.channelinterceptor;

import cn.maiaimei.samples.channelinterceptor.config.ChannelInterceptorConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import(ChannelInterceptorConfig.class)
//@ImportResource(locations = "classpath:beans/channel-interceptor.xml")
public class ChannelInterceptorAutoConfiguration {
}
