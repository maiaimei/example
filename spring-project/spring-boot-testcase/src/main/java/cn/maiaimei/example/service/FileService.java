package cn.maiaimei.example.service;

import cn.maiaimei.example.util.FileHelper;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public class FileService {
    private final FileHelper fileHelper;

    public FileService(FileHelper fileHelper) {
        this.fileHelper = fileHelper;
    }

    public File create(String pathname) {
        return fileHelper.create(pathname);
    }
    
    public String m1(String pathname) {
        File file = fileHelper.create(pathname);
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getPath();
    }

    public String m2(String pathname) {
        File file = fileHelper.create(pathname);
        if (!fileHelper.exists(file)) {
            fileHelper.mkdir(file);
        }
        return fileHelper.getPath(file);
    }
}
