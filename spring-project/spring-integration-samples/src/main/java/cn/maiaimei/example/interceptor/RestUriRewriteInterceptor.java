package cn.maiaimei.example.interceptor;

import cn.maiaimei.framework.rest.RestLoggingInterceptor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

@Data
@Component
@ConfigurationProperties(prefix = "external-system.example")
public class RestUriRewriteInterceptor extends RestLoggingInterceptor {

    private Map<String, String> env;

    @Override
    protected HttpRequest getHttpRequestWrapper(HttpRequest request) {
        return new RestUriRewriteRequestWrapper(request, env);
    }

    private static class RestUriRewriteRequestWrapper extends HttpRequestWrapper {
        private final Map<String, String> env;
        private final String activeEnv;

        /**
         * Create a new {@code HttpRequest} wrapping the given request object.
         *
         * @param request the request object to be wrapped
         */
        public RestUriRewriteRequestWrapper(HttpRequest request, Map<String, String> env) {
            super(request);
            this.env = env;
            this.activeEnv = Optional.ofNullable(request.getHeaders().get("env")).orElseGet(ArrayList::new).get(0);
        }

        @Override
        public URI getURI() {
            String prefix = "https://exampleTemporaryHost/";
            final String originalUri = super.getURI().toString();
            if (!originalUri.startsWith(prefix)) {
                return super.getURI();
            }
            final String baseUrl = env.get(activeEnv);
            final String path = super.getURI().getPath();
            String targetUrl = baseUrl.concat("/").concat(path);
            return URI.create(targetUrl);
        }
    }
}
