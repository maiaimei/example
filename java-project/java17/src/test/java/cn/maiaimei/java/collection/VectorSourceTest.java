package cn.maiaimei.java.collection;

import org.junit.jupiter.api.Test;

import java.util.Vector;

/**
 * Vector：元素有序（元素添加顺序与取出顺序一致）且可重复，支持索引获取元素
 * 使用 Object[] elementData 存储元素，在添加元素时需要动态扩容，以无参构造方法创建Vector实例，每次扩容为Vector容量的2倍
 * 在删除元素时，需要移动元素
 * 线程安全，效率低
 */
@SuppressWarnings("all")
public class VectorSourceTest {
    @Test
    void testAddNoInitialCapacity(){
        final Vector list = new Vector();
        for (int i = 1; i <= 10; i++) {
            list.add(i);
        }
        for (int i = 11; i <= 20; i++) {
            list.add(i);
        }
        for (int i = 21; i <= 40; i++) {
            list.add(i);
        }
        list.add(41);
        list.add(42);
    }

    @Test
    void testAddWithInitialCapacity(){
        final Vector list = new Vector(8);
        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }
        for (int i = 9; i <= 16; i++) {
            list.add(i);
        }
        for (int i = 17; i <= 24; i++) {
            list.add(i);
        }
        list.add(25);
        list.add(26);
    }

    @Test
    void testAddWithInitialCapacityAndCapacityIncrement(){
        final Vector list = new Vector(8,10);
        for (int i = 1; i <= 8; i++) {
            list.add(i);
        }
        for (int i = 9; i <= 18; i++) {
            list.add(i);
        }
        for (int i = 19; i <= 28; i++) {
            list.add(i);
        }
        list.add(29);
        list.add(30);
    }

    @Test
    void testSet(){
        final Vector list = new Vector();
        list.add("1");
        list.add("2");
        list.add("3");
        list.set(1,"second");
    }

    @Test
    void testRemove(){
        final Vector list = new Vector();
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
        final Vector list = new Vector();
        list.add("1");
        list.add("2");
        list.clear();
    }
}
