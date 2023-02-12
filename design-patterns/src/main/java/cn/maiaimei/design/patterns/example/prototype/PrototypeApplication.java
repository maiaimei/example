package cn.maiaimei.design.patterns.example.prototype;

/**
 * 原型模式 (Prototype)，创建型设计模式，通过克隆、拷贝、复制对象创建新的对象。
 * 深拷贝 VS 浅拷贝：
 * 在浅拷贝中，对基本数据类型，浅拷贝会进行值传递，也就是将该属性值复制一份给新对象；对于引用数据类型，浅拷贝会进行引用传递，两个对象指向同一个地址。典型应用：{@link Object#clone()}
 * 在深拷贝中，无论原型对象的成员变量是值类型还是引用类型，都将复制一份给克隆对象。
 */
public class PrototypeApplication {
    public static void main(String[] args) throws CloneNotSupportedException {
        User user = new User();
        user.setUsername("admin");
        user.setPassword("12345");

        Object clone = user.clone();

        System.out.println(user);
        System.out.println(clone);
    }

    static class User implements Cloneable {
        private String username;
        private String password;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }
    }
}
