package cn.maiaimei.example.service;

import cn.maiaimei.example.util.Axios;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DemoServiceTest {
    @InjectMocks
    DemoService demoService;

    @Mock
    OtherService otherService;

    @Mock
    ThreadPoolTaskExecutor asyncTaskExecutor;

    @Spy
    Axios axios;
    
    @Test
    void testMethodA() {
        when(otherService.methodA()).thenReturn("hi");
        when(otherService.methodB()).thenReturn("java");
        // 新增对异步线程里面Runnable方法的驱动
        doAnswer(
                (InvocationOnMock invocation) -> {
                    ((Runnable) invocation.getArguments()[0]).run();
                    return null;
                }
        ).when(asyncTaskExecutor).execute(any(Runnable.class));
        String actual = demoService.methodA();
        assertEquals("hi, java", actual);
    }

    @SneakyThrows
    @Test
    void testMethodB() {
        Field asyncTaskExecutorField = Axios.class.getDeclaredField("asyncTaskExecutor");
        asyncTaskExecutorField.setAccessible(Boolean.TRUE);
        asyncTaskExecutorField.set(axios, asyncTaskExecutor);
        asyncTaskExecutorField.setAccessible(Boolean.FALSE);

        when(otherService.methodA()).thenReturn("hi");
        when(otherService.methodB()).thenReturn("java");
        // 新增对异步线程里面Runnable方法的驱动
        doAnswer(
                (InvocationOnMock invocation) -> {
                    ((Runnable) invocation.getArguments()[0]).run();
                    return null;
                }
        ).when(this.asyncTaskExecutor).execute(any(Runnable.class));
        String actual = demoService.methodB();
        assertEquals("hi, java", actual);
    }
}