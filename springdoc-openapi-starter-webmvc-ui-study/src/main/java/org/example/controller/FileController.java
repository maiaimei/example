package org.example.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/files")
public class FileController {

  @GetMapping(value = "/download", produces = "application/octet-stream")
  public ResponseEntity<Resource> downloadFile() {
    // 文件下载逻辑
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"file.pdf\"")
        .body(new ByteArrayResource(new byte[0]));
  }

  @PostMapping(value = "/upload", consumes = "multipart/form-data")
  public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
    // 文件上传逻辑
    return ResponseEntity.ok("File uploaded successfully");
  }

  @GetMapping(value = "/binary", produces = "application/octet-stream")
  public byte[] getBinaryData() {
    // 二进制数据返回逻辑
    return new byte[0];
  }
}