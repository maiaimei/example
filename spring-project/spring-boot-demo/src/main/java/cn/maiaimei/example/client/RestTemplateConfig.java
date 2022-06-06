package cn.maiaimei.example.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.Charset;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

@Configuration
public class RestTemplateConfig {
    @Bean
    public HttpClient httpClient() {
        return new HttpClient();
    }

    //@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public RestTemplate restTemplate(ClientHttpRequestFactory httpRequestFactory) {
        RestTemplate restTemplate = new RestTemplate();
        List<HttpMessageConverter<?>> messageConverters = restTemplate.getMessageConverters();
        Iterator<HttpMessageConverter<?>> iterator = messageConverters.iterator();
        while (iterator.hasNext()) {
            HttpMessageConverter<?> converter = iterator.next();
            if (converter instanceof StringHttpMessageConverter) {
                // StringHttpMessageConverter默认使用ISO-8859-1编码，此处修改为UTF-8
                ((StringHttpMessageConverter) converter).setDefaultCharset(Charset.forName("UTF-8"));
            }
        }
        // 添加拦截器
        restTemplate.setInterceptors(Arrays.asList(new LoggingRequestInterceptor()));
        // 通过BufferingClientHttpRequestFactory对象包装现有的RequestFactory，用来支持多次调用getBody()方法
        restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(httpRequestFactory));
        return restTemplate;
    }

    @Bean
    public ClientHttpRequestFactory simpleClientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        //factory.setReadTimeout(150000);
        //factory.setConnectTimeout(150000);
        return factory;
    }
}
