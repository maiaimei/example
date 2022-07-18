package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@RestController
@RequestMapping(("/client"))
public class ClientController {
    private static final String baseUrl = "http://localhost:8080";

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/pagingQuery")
    public List<User> pagingQuery(@RequestParam Long current, @RequestParam Long size) {
        ParameterizedTypeReference responseTypeReference = new ParameterizedTypeReference<List<User>>() {
        };
        ResponseEntity responseEntity = restTemplate.exchange(baseUrl + "/pagingQuery", HttpMethod.GET, null, responseTypeReference);
        return null;
    }

    @GetMapping("/{id}")
    public User get(@PathVariable Long id) {
        return null;
    }
}
