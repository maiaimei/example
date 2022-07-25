package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@ExtendWith(MockitoExtension.class)
public class RestTemplateTest {
    @Test
    public void test() {
        RestTemplate api = Mockito.mock(RestTemplate.class);
        ResponseEntity<?> response1 = Mockito.mock(ResponseEntity.class);
        ResponseEntity<?> response2 = Mockito.mock(ResponseEntity.class);

        Mockito.when(api.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(Class.class))).thenReturn(response1);
        Mockito.when(api.exchange(any(String.class), any(HttpMethod.class), any(HttpEntity.class), any(ParameterizedTypeReference.class))).thenReturn(response2);

        ParameterizedTypeReference parameterizedTypeReference = Mockito.mock(ParameterizedTypeReference.class);

        assertEquals(response1, api.exchange("", HttpMethod.GET, new HttpEntity(""), String.class));
        assertEquals(response2, api.exchange("", HttpMethod.GET, new HttpEntity(""), parameterizedTypeReference));
    }
}
