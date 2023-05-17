package cn.maiaimei.example;

import com.google.common.collect.Maps;
import org.checkerframework.checker.nullness.qual.Nullable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SuppressWarnings("all")
@ExtendWith(MockitoExtension.class)
public class MockitoTest {
    @Mock
    private List list;

    @Test
    public void testAdd() {
        list.add(1);

        // 断言
        verify(list, times(1)).add(any());
        verify(list, atLeast(1)).add(any());
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

    /**
     * mockito-core不支持mock静态方法，需要将mockito-core替换为mockito-inline
     */
    @Test
    public void testStaticMethod(){
        mockStatic(Maps.class);
        HashMap<Object, Object> expectedMap = new HashMap<>();
        when(Maps.newHashMap()).thenReturn(expectedMap);
        final HashMap<String, Object> map = Maps.newHashMap();
        assertEquals(expectedMap,map);
    }
}
