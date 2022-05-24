package cn.maiaimei.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class Demo03Test {
    @Mock
    private List list;

    @Test
    public void testAdd() {
        list.add(1);

        // 断言
        //verify(list, times(1)).add(any());
        //verify(list, atLeast(1)).add(any());
        verify(list, atLeastOnce()).add(any());
    }

    @Test
    public void testGet() {
        // 先打桩
        when(list.get(0)).thenReturn(1);

        // 中间执行目标方法
        list.add("one");

        // 后断言
        assertEquals(1, list.get(0));
    }
}
