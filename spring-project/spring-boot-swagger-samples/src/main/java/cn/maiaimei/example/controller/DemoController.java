package cn.maiaimei.example.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Api
@Slf4j
@RestController
public class DemoController {

    @PostMapping(value = "/upload/file", consumes = "multipart/form-data")
    public void uploadFile(@RequestPart(name = "file") MultipartFile file) {
        log.info("originalFilename: {}, size: {}, contentType: {}",
                file.getOriginalFilename(),
                file.getSize(),
                file.getContentType());
    }

    /**
     * consumes = "multipart/form-data" 解决org.springframework.web.multipart.MultipartException: Current request is not a multipart request
     */
    @PostMapping(value = "/upload/file", consumes = "multipart/form-data")
    public void uploadMultipleFiles(@RequestPart(name = "files") MultipartFile[] files,
                                    @RequestParam String p1,
                                    @RequestParam String p2) {
        log.info("p1: {}, p2: {}", p1, p2);
        log.info("file count: {}", files.length);
        for (MultipartFile file : files) {
            log.info("originalFilename: {}, size: {}, contentType: {}",
                    file.getOriginalFilename(),
                    file.getSize(),
                    file.getContentType());
        }
    }

}
