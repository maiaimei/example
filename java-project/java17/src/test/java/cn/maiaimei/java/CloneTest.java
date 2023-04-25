package cn.maiaimei.java;

import java.util.Arrays;
import java.util.List;

/**
 * 使用Object#clone方法必须实现Cloneable接口
 * 对于基本数据类型拷贝的是值本身
 * 对于引用数据类型拷贝的是内存地址
 * 也就是说，Object#clone是浅拷贝
 * 浅拷贝 VS 深拷贝
 */
public class CloneTest {

    public static void main(String[] args) throws CloneNotSupportedException {
        final Computer computer = new Computer();
        computer.setBrand("ThinkPad");
        computer.setAccessories(Arrays.asList("mouse","keyboard"));
        final Computer c = computer.clone();
        System.out.println(computer.getAccessories() == c.getAccessories());
    }
    
    private static class Computer implements Cloneable {
        private String brand;
        private List<String> accessories;

        public String getBrand() {
            return brand;
        }

        public void setBrand(String brand) {
            this.brand = brand;
        }

        public List<String> getAccessories() {
            return accessories;
        }

        public void setAccessories(List<String> accessories) {
            this.accessories = accessories;
        }

        @Override
        protected Computer clone() throws CloneNotSupportedException {
            return (Computer) super.clone();
        }
    }
}
