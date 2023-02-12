package cn.maiaimei.design.patterns.example.builder;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 建造者模式/生成器模式(builder)，将一个复杂对象的构建与它的表示分离，使得同样的构建过程可以创建不同的表示。
 */
@Slf4j
public class BuilderApplication {

    public static void main(String[] args) {
        Director director = new Director();
        Builder builder1 = new ConcreteBuilder1();
        Builder builder2 = new ConcreteBuilder2();

        director.construct(builder1);
        builder1.getProduct().show();

        director.construct(builder2);
        builder2.getProduct().show();

        User user = User.builder().username("admin").password("123456").build();
        System.out.println(user);
    }

    static class Product {
        private final List<String> parts = new ArrayList<>();

        public void addPart(String part) {
            this.parts.add(part);
        }

        public void show() {
            log.info("构建产品");
            for (String part : parts) {
                log.info(part);
            }
        }
    }

    interface Builder {
        void buildPart1();

        void buildPart2();

        void buildPart3();

        Product getProduct();
    }

    static class ConcreteBuilder1 implements Builder {
        private final Product product = new Product();

        @Override
        public void buildPart1() {
            product.addPart("部件 A");
        }

        @Override
        public void buildPart2() {
            product.addPart("部件 B");
        }

        @Override
        public void buildPart3() {
            product.addPart("部件 C");
        }

        @Override
        public Product getProduct() {
            return product;
        }
    }

    static class ConcreteBuilder2 implements Builder {
        private final Product product = new Product();

        @Override
        public void buildPart1() {
            product.addPart("部件 X");
        }

        @Override
        public void buildPart2() {
            product.addPart("部件 Y");
        }

        @Override
        public void buildPart3() {
            product.addPart("部件 Z");
        }

        @Override
        public Product getProduct() {
            return product;
        }
    }

    static class Director {
        public void construct(Builder builder) {
            builder.buildPart1();
            builder.buildPart2();
            builder.buildPart3();
        }
    }

    /**
     * lombok 的 @Builder
     */
    static class User {
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
        public String toString() {
            return "User{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    '}';
        }

        public static UserBuilder builder() {
            return new UserBuilder();
        }

        public User() {

        }

        public User(UserBuilder builder) {
            this.username = builder.username;
            this.password = builder.password;
        }

        static class UserBuilder {
            private String username;
            private String password;

            public UserBuilder username(String username) {
                this.username = username;
                return this;
            }

            public UserBuilder password(String password) {
                this.password = password;
                return this;
            }

            public User build() {
                return new User(this);
            }
        }
    }

}
