package cn.maiaimei.example.config;

import cn.maiaimei.example.interceptor.RestUriRewriteInterceptor;
import cn.maiaimei.framework.rest.RestErrorHandler;
import cn.maiaimei.framework.rest.RestTemplateFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class RestConfig {
    
    @Bean
    public RestErrorHandler restErrorHandler() {
        return new RestErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(RestUriRewriteInterceptor restUriRewriteInterceptor, RestErrorHandler restErrorHandler) throws NoSuchAlgorithmException, KeyStoreException, KeyManagementException {
        return RestTemplateFactory.getRestTemplate(restUriRewriteInterceptor, restErrorHandler);
    }

}

