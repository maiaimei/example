package cn.maiaimei.example.client;

import static cn.maiaimei.example.constant.Constants.TRACE_ID;

import cn.maiaimei.example.utils.TraceIdUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Component
public class HttpClient extends AbstractHttpClient {

  public HttpClient(RestTemplate restTemplate) {
    super(restTemplate);
  }

  @Override
  protected MultiValueMap<String, String> getHeaders() {
    MultiValueMap<String, String> headers = new LinkedMultiValueMap<>();
    headers.set(TRACE_ID, TraceIdUtils.getOrSetTraceId());
    return headers;
  }
}
