package cn.maiaimei.java.collection;

import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * LinkedList：元素有序（元素添加顺序与取出顺序一致）且可重复，支持索引获取元素
 * 使用双向链表存储元素，添加或删除元素较快，不需要移动
 * 线程不安全，效率高
 */
@SuppressWarnings("all")
public class LinkedListSourceTest {
    @Test
    void testAdd(){
        final LinkedList list = new LinkedList();
        list.add("2");
        list.add("3");
        list.add("4");
        list.addFirst("1");
        list.addLast("5");
        System.out.println(list); // [1, 2, 3, 4, 5]
    }

    @Test
    void testRemove(){
        final LinkedList list = new LinkedList();
        list.add("2");
        list.add("3");
        list.add("4");
        list.addFirst("1");
        list.addLast("5");
        list.add("6");
        System.out.println(list); // [1, 2, 3, 4, 5, 6]
        list.remove();
        System.out.println(list); // [2, 3, 4, 5, 6]
        list.removeFirst(); // [3, 4, 5, 6]
        System.out.println(list);
        list.removeLast();
        System.out.println(list); // [3, 4, 5]
        list.remove(1);
        System.out.println(list); // [3, 5]
    }
}
