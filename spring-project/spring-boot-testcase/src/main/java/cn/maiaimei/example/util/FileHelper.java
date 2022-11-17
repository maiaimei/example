package cn.maiaimei.example.util;

import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class FileHelper {
    public File create(String pathname) {
        return new File(pathname);
    }

    public File create(String parent, String child) {
        return new File(parent, child);
    }

    public boolean exists(File file) {
        return file.exists();
    }

    public boolean mkdir(File file) {
        return file.mkdir();
    }

    public boolean mkdirs(File file) {
        return file.mkdirs();
    }

    @SneakyThrows
    public boolean createNewFile(File file) {
        return file.createNewFile();
    }

    public String getPath(File file) {
        return file.getPath();
    }

    public String getAbsolutePath(File file) {
        return file.getAbsolutePath();
    }

    public String getSystemTmpDir() {
        return System.getProperty("java.io.tmpdir");
    }
}
