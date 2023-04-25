package cn.maiaimei.java;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class CollectionTest {
    /**
     * List：有序，元素可重复
     * 有序：插入元素顺序跟输出元素顺序一致
     */
    @Test
    void testArrayList01(){
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

    /**
     * Set：无序，元素无重复
     * 无序：插入元素顺序跟输出元素顺序可能不一致
     */
    @Test
    void testHashSet01(){
        HashSet<Integer> integers = new HashSet<>();
        integers.add(1);
        integers.add(100);
        integers.add(33);
        integers.add(78);
        integers.add(33);
        for (Integer integer : integers) {
            System.out.println(integer);
        }
    }

    /**
     * for-each 与 迭代器
     */
    @Test
    void testIterator01(){
        final ArrayList<Integer> integers = mockIntegers();
        // for-each 底层使用了迭代器
        for (Integer integer : integers) {
            System.out.println(integer);
        }
        // 迭代器遍历
        final Iterator<Integer> iterator = integers.iterator();
        while (iterator.hasNext()){
            System.out.println(iterator.next());
        }
    }

    /**
     * 使用iterator遍历collection时，collection不能增删改
     */
    @Test
    void testIterator02(){
        final ArrayList<Integer> integers = mockIntegers();
        final Iterator<Integer> iterator = integers.iterator();
        //integers.add(999);
        integers.remove(0);
        iterator.next(); // java.util.ConcurrentModificationException
    }

    ArrayList<Integer> mockIntegers(){
        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(1);
        integers.add(100);
        integers.add(33);
        integers.add(78);
        integers.add(33);
        return integers;
    }
}
