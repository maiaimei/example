package cn.maiaimei.example.controller;

import cn.hutool.core.io.FileUtil;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@RestController
@RequestMapping("/local")
public class LocalController {
    @Value("${app.upload-location}")
    private String uploadLocation;

    @PostMapping("/upload/file")
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        doUploadFile(file);
    }

    @PostMapping("/upload/files")
    public void uploadFiles(@RequestParam(name = "files") MultipartFile[] files) {
        for (MultipartFile file : files) {
            doUploadFile(file);
        }
    }

    @SneakyThrows
    private void doUploadFile(MultipartFile file) {
        String dirName = String.format("%s/%s", uploadLocation, LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE));
        File uploadFolder = new File(dirName);
        if (!uploadFolder.exists()) {
            uploadFolder.mkdirs();
        }

        String targetFilename = UUID.randomUUID().toString().replaceAll("-", "") + "." + FileUtil.getSuffix(file.getOriginalFilename());

        file.transferTo(new File(uploadFolder, targetFilename));
    }
}
