package cn.maiaimei.example.controller;

import cn.maiaimei.example.model.BusinessData;
import cn.maiaimei.example.utils.JsonUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class FileController {

  @PostMapping("/upload/file")
  public String uploadFile(MultipartFile file, BusinessData businessData) {
    log.info("上传单个文件1");
    final Map<String, Object> map = new HashMap<>();
    map.put("file", getFileInfo(file));
    map.put("businessData", businessData);
    return JsonUtils.stringify(map);
  }

  @PostMapping("/upload/files")
  public String uploadFiles(@RequestPart List<MultipartFile> files, BusinessData businessData) {
    log.info("上传多个文件1");
    List<Map<String, String>> infos = new ArrayList<>();
    for (MultipartFile file : files) {
      infos.add(getFileInfo(file));
    }
    final Map<String, Object> map = new HashMap<>();
    map.put("files", infos);
    map.put("businessData", businessData);
    return JsonUtils.stringify(map);
  }

  @SneakyThrows
  private Map<String, String> getFileInfo(MultipartFile file) {
    Map<String, String> map = new HashMap<>();
    //map.put("name", file.getName()); // Return the name of the parameter in the multipart form.
    map.put("originalFilename", file.getOriginalFilename());
    map.put("size", String.valueOf(file.getSize()));
    map.put("contentType", file.getContentType());
    return map;
  }

}
