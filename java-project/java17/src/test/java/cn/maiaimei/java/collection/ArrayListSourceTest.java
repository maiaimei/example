package cn.maiaimei.java.collection;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

/**
 * ArrayList：元素有序（元素添加顺序与取出顺序一致）且可重复，支持索引获取元素
 * 使用 Object[] elementData 存储元素，在添加元素时需要动态扩容，以无参构造方法创建ArrayList实例，每次扩容为ArrayList容量的1.5倍
 * 在删除元素时，需要移动元素
 * 线程不安全，效率高
 */
@SuppressWarnings("all")
public class ArrayListSourceTest {

    @Test
    void testForEach(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(100);
        integers.add(33);
        integers.add(78);
        integers.add(33);
        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }
    
    @Test
    void testRightShift(){
        int capacity = 8;
        System.out.printf("第%s次扩容数组容量是%d\n", 1, capacity);
        for (int i = 2; i <= 5; i++) {
            int growth = capacity >> 1;
            capacity += growth;
            System.out.printf("第%s次扩容数组容量是%d\n", i, capacity);
        }
    }
    
    @Test
    void testAddNoInitialCapacity(){
        final ArrayList list = new ArrayList();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        for (int i = 11; i <= 15; i++) {
            list.add(i);
        }
        for (int i = 16; i <= 22; i++) {
            list.add(i);
        }
        list.add(23);
        list.add(24);
    }

    @Test
    void testAddWithInitialCapacity(){
        final ArrayList list = new ArrayList(8);
        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }
        for (int i = 9; i <= 12; i++) {
            list.add(i);
        }
        for (int i = 13; i <= 18; i++) {
            list.add(i);
        }
        list.add(19);
        list.add(10);
    }
    
    @Test
    void testSet(){
        final ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.set(1,"second");
    }
    
    @Test
    void testRemove(){
        final ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
        list.add("5");
        //list.remove("1");
        list.remove(1);
    }
    
    @Test
    void testClear(){
        final ArrayList list = new ArrayList();
        list.add("1");
        list.add("2");
        list.clear();
    }
}
