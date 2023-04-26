package cn.maiaimei.java.collection;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.HashSet;

@SuppressWarnings("all")
public class HashSetSourceTest {
    @Test
    void testAdd(){
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        final String[] alphabets = alphabet.split("");

        final HashSet hashSet = new HashSet();
        for (String letter : alphabets) {
            hashSet.add(letter);
        }
        for (String letter : alphabets) {
            hashSet.add(letter.toUpperCase());
        }
        for (int i = 0; i <= 9; i++) {
            hashSet.add(String.valueOf(i));
        }
        // 链表
        add(hashSet,127,48,7);
        // 红黑树
        add(hashSet,127,50,9);
        System.out.println(hashSet);
    }
    
    void add(HashSet hashSet, int size, int index, int targetCount){
        int count = 0;
        for (int i = 1;; i++) {
            Integer value = i*index;
            if(index(size,value) == index) {
                hashSet.add(value);
                count++;
            }
            if(count == targetCount){
                break;
            }
        }
    }
    
    int index(int n, Object key){
        return (n - 1) & hash(key);
    }

    int hash(Object key) {
        int h;
        return (key == null) ? 0 : (h = key.hashCode()) ^ (h >>> 16);
    }
}
