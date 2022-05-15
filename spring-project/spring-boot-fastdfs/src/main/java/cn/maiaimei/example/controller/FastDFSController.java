package cn.maiaimei.example.controller;

import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.maiaimei.example.model.FileModel;
import cn.maiaimei.example.service.FileService;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/fastdfs")
public class FastDFSController {
    @Autowired
    private FileService fileService;

    @Autowired
    private FastFileStorageClient storageClient;

    @RequestMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        FileModel fileModel = fileService.getById(id);
        storageClient.deleteFile(fileModel.getGroupName(), fileModel.getPath());
        fileService.removeById(id);
    }

    @SneakyThrows
    @RequestMapping("/download/{id}")
    public void download(@PathVariable Long id, HttpServletResponse response) {
        FileModel fileModel = fileService.getById(id);
        byte[] bytes = storageClient.downloadFile(fileModel.getGroupName(), fileModel.getPath(), new DownloadByteArray());
        response.reset();
        response.setContentType("application/octet-stream");
        response.addHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileModel.getOriginalFilename(), "UTF-8"));
        ServletOutputStream outputStream = response.getOutputStream();
        outputStream.write(bytes);
        outputStream.flush();
    }

    @PostMapping("/upload/file")
    public void uploadFile(@RequestParam(name = "file") MultipartFile file) {
        StorePath storePath = doUploadFile(file);
        fileService.create(buildFileModel(file, storePath));
    }

    @PostMapping("/upload/files")
    public void uploadFiles(@RequestParam(name = "files") MultipartFile[] files) {
        List<FileModel> fileModels = new ArrayList<>();
        for (MultipartFile file : files) {
            StorePath storePath = doUploadFile(file);
            fileModels.add(buildFileModel(file, storePath));
        }
        fileService.batchCreate(fileModels);
    }

    @SneakyThrows
    private StorePath doUploadFile(MultipartFile file) {
        String suffix = FileUtil.getSuffix(file.getOriginalFilename());
        return storageClient.uploadFile(file.getInputStream(), file.getSize(), suffix, null);
    }

    @SneakyThrows
    private FileModel buildFileModel(MultipartFile file, StorePath storePath) {
        FileModel fileModel = new FileModel();
        fileModel.setOriginalFilename(file.getOriginalFilename());
        fileModel.setType(FileTypeUtil.getType(file.getInputStream(), file.getOriginalFilename()));
        fileModel.setSize(file.getSize());
        fileModel.setGroupName(storePath.getGroup());
        fileModel.setPath(storePath.getPath());
        return fileModel;
    }
}
