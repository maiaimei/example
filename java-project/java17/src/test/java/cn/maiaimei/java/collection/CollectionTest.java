package cn.maiaimei.java.collection;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

public class CollectionTest {
    
    @SuppressWarnings("all")
    @Test
    void testCollectionMethod(){
        Collection collection = new ArrayList();
        // 添加元素
        collection.add("Java");
        collection.add(999);
        collection.add(true);
        System.out.println(collection);
        // 删除元素
        collection.remove(999);
        System.out.println(collection);
        // 清空集合
        collection.clear();
        System.out.println(collection);
        // 判断集合是否为空
        System.out.println(collection.isEmpty());
        // 获取集合元素个数
        System.out.println(collection.size());
        // 判断元素是否存在
        System.out.println(collection.contains(999));
        Collection coll = new ArrayList();
        coll.add("Redis");
        coll.add("MySQL");
        System.out.println(collection.containsAll(coll));
        collection.addAll(coll);
        System.out.println(collection.containsAll(coll));
        System.out.println(collection);
        collection.removeAll(coll);
        System.out.println(collection.containsAll(coll));
        System.out.println(collection);
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
