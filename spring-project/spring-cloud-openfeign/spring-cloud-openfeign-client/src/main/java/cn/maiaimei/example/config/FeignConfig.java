package cn.maiaimei.example.config;

import cn.maiaimei.example.component.FeignClientRequestInterceptor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.security.cert.X509Certificate;

@Slf4j
@Configuration
@EnableFeignClients(basePackages = "cn.maiaimei.example.client")
public class FeignConfig {

    /**
     * TODO: https://www.jb51.net/article/251793.htm
     *
     * @return
     */
    @SneakyThrows
    //@Bean
    public FeignClientRequestInterceptor feignClientRequestInterceptor() {
        // 忽略SSL校验
        SSLContext ctx = SSLContext.getInstance("SSL");
        X509TrustManager tm = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public void checkServerTrusted(X509Certificate[] chain, String authType) {
            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        ctx.init(null, new TrustManager[]{tm}, null);
        //return new FeignClientRequestInterceptor(ctx.getSocketFactory(), (hostname, sslSession) -> true);
        return new FeignClientRequestInterceptor(ctx.getSocketFactory(), (hostname, sslSession) -> false);
    }
}