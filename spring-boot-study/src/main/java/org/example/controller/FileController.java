package org.example.controller;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class FileController {

  // 单文件上传保持简单路径
  @PostMapping(value = "/files/upload/single", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadFile(@RequestPart MultipartFile file) {
    final String message = "File %s uploaded successfully".formatted(file.getOriginalFilename());
    log.info(message);
    return message;
  }

  // 使用 batch 表示批量上传
  @PostMapping(value = "/files/upload/batch", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadFileArray(@RequestPart MultipartFile[] files) {
    for (MultipartFile file : files) {
      log.info("File {} uploaded successfully", file.getOriginalFilename());
    }
    return "File upload successful, number of files: %d".formatted(files.length);
  }

  // 使用 multiple 表示多文件上传
  @PostMapping(value = "/files/upload/multiple", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public String uploadFileList(@RequestPart List<MultipartFile> files) {
    for (MultipartFile file : files) {
      log.info("File {} uploaded successfully", file.getOriginalFilename());
    }
    return "File upload successful, number of files: %d".formatted(files.size());
  }

}
