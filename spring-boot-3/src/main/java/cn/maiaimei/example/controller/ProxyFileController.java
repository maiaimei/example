package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.BusinessData;
import cn.maiaimei.example.utils.JsonUtils;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/proxy")
public class ProxyFileController {

  private static final String BASE_PATH = "http://localhost:8080";

  @Autowired
  private RestTemplate restTemplate;

  @PostMapping("/upload/file")
  public String uploadFile(MultipartFile file, BusinessData businessData) {
    log.info("上传单个文件2");
    // 设置请求的头部
    HttpHeaders headers = new HttpHeaders();
    //headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    // 设置请求体
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.add("file", file.getResource());
    body.addAll(JsonUtils.toMultiValueMap(businessData));
    // 创建HttpEntity
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    return restTemplate.postForObject(BASE_PATH.concat("/upload/file"), requestEntity,
        String.class);
  }

  @PostMapping("/upload/files")
  public String uploadFiles(@RequestPart List<MultipartFile> files, BusinessData businessData) {
    log.info("上传多个文件2");
    // 设置请求的头部
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.MULTIPART_FORM_DATA);
    // 设置请求体
    MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
    body.put("files", files.stream().map(MultipartFile::getResource).collect(Collectors.toList()));
    body.addAll(JsonUtils.toMultiValueMap(businessData));
    // 创建HttpEntity
    HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
    return restTemplate.postForObject(BASE_PATH.concat("/upload/files"), requestEntity,
        String.class);
  }

}
