package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class FileTest {

    @Test
    public void test_exists(){
        final File mockFile = Mockito.mock(File.class);
        when(mockFile.exists()).thenReturn(Boolean.TRUE);
        assertTrue(mockFile.exists());
    }
    @Test
    public void test_isFile(){
        final File mockFile = Mockito.mock(File.class);
        when(mockFile.isFile()).thenReturn(Boolean.TRUE);
        assertTrue(mockFile.isFile());
    }

    @Test
    public void test_isDirectory(){
        final File mockFile = Mockito.mock(File.class);
        when(mockFile.isDirectory()).thenReturn(Boolean.TRUE);
        assertTrue(mockFile.isDirectory());
    }

    @Test
    public void test_getPath(){
        String path = "/path/to";
        final File mockFile = Mockito.mock(File.class);
        when(mockFile.getPath()).thenReturn(path);
        assertEquals(path, mockFile.getPath());
    }

    @Test
    public void test_getName(){
        String name = "test.txt";
        final File mockFile = Mockito.mock(File.class);
        when(mockFile.getName()).thenReturn(name);
        assertEquals(name, mockFile.getName());
    }

    
}
