package cn.maiaimei.example.service;

import cn.maiaimei.example.util.FileHelper;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
public class FileServiceTest {
    @Autowired
    private FileService fileService;
    @MockBean
    private FileHelper fileHelper;

    @Test
    void testCreate() {
        File mockFile = Mockito.mock(File.class);
        when(fileHelper.create(anyString())).thenReturn(mockFile);
        String pathname = "C:\\test\\";
        File file = fileService.create(pathname);
        assertEquals(mockFile, file);
    }

    /**
     * Recommended for testcase
     */
    @Test
    void testM1() {
        File mockFile = Mockito.mock(File.class);
        String pathname = "C:\\test\\";
        when(fileHelper.create(anyString())).thenReturn(mockFile);
        when(mockFile.exists()).thenReturn(Boolean.FALSE);
        when(mockFile.mkdir()).thenReturn(Boolean.TRUE);
        when(mockFile.getPath()).thenReturn(pathname);
        String path = fileService.m1(pathname);
        assertEquals(pathname, path);
    }

    @Test
    void testM2() {
        File mockFile = Mockito.mock(File.class);
        String pathname = "C:\\test\\";
        when(fileHelper.create(anyString())).thenReturn(mockFile);
        when(fileHelper.exists(any())).thenReturn(Boolean.FALSE);
        when(fileHelper.mkdir(any())).thenReturn(Boolean.TRUE);
        when(fileHelper.getPath(any())).thenReturn(pathname);
        String path = fileService.m2(pathname);
        assertEquals(pathname, path);
    }
    
    @Test
    void testMockFile() {
        File mockFile = Mockito.mock(File.class);

        when(mockFile.exists()).thenReturn(Boolean.TRUE);
        when(mockFile.isFile()).thenReturn(Boolean.TRUE);
        when(mockFile.isDirectory()).thenReturn(Boolean.FALSE);
        when(mockFile.getPath()).thenReturn("/test");

        assertEquals(Boolean.TRUE, mockFile.exists());
        assertEquals(Boolean.TRUE, mockFile.isFile());
        assertEquals(Boolean.FALSE, mockFile.isDirectory());
        assertEquals("/test", mockFile.getPath());
    }

    /**
     * new File(...)：并不会创建文件，new File(...)创建的文件实例调用mkdir/mkdirs后会创建目录，调用createNewFile会创建文件
     * mkdir：创建目录
     * mkdirs：创建级联目录
     * createNewFile：创建文件
     */
    @SneakyThrows
    //@Test
    void testApi() {
        File directory = new File("C:\\Users\\lenovo\\AppData\\Local\\Temp\\test\\a\\1.txt");
        System.out.println("file.exists() = " + directory.exists());
        System.out.println("file.isFile() = " + directory.isFile());
        System.out.println("file.isDirectory() = " + directory.isDirectory());
        directory.createNewFile();
        System.out.println("file.exists() = " + directory.exists());
        System.out.println("file.isFile() = " + directory.isFile());
        System.out.println("file.isDirectory() = " + directory.isDirectory());
    }
}
