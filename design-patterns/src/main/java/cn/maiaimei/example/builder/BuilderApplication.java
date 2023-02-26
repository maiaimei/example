package cn.maiaimei.example.builder;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

/**
 * 建造者模式/生成器模式(builder)，
 * 将一个复杂对象的构建与它的表示分离，
 * 使得同样的构建过程可以创建不同的表示。
 */
@Slf4j
public class BuilderApplication {

    public static void main(String[] args) {
    	Director director = new Director();
    	Product productA = new Product("产品A");
    	Product productB = new Product("产品B");
        Builder builderA = new ConcreteBuilderA(productA);
        Builder builderB = new ConcreteBuilderB(productB);

        director.construct(builderA);
        productA.show();

        director.construct(builderB);
        productB.show();

        User user = User.builder().username("admin").password("123456").build();
        log.info("{}",user);
    }

    static class Product {
    	private String name;
    	
        private final List<String> parts = new ArrayList<>();
        
        public Product(String name) {
        	this.name = name;
        }

        public void addPart(String part) {
            this.parts.add(part);
        }
        
        public void show() {
            log.info("{}",name);
            for (String part : parts) {
                log.info(part);
            }
        }
    }

    interface Builder {
        void buildPartC();

        void buildPartB();

        void buildPartA();
    }

    static class ConcreteBuilderA implements Builder {
        private final Product product;
        
        public ConcreteBuilderA(Product product) {
        	this.product = product;
        }

        @Override
        public void buildPartC() {
            product.addPart("部件 3");
        }

        @Override
        public void buildPartB() {
            product.addPart("部件 2");
        }

        @Override
        public void buildPartA() {
            product.addPart("部件 1");
        }
    }

    static class ConcreteBuilderB implements Builder {
        private final Product product;
        
        public ConcreteBuilderB(Product product) {
        	this.product = product;
        }

        @Override
        public void buildPartC() {
            product.addPart("部件 Z");
        }

        @Override
        public void buildPartB() {
            product.addPart("部件 Y");
        }

        @Override
        public void buildPartA() {
            product.addPart("部件 X");
        }
    }

    static class Director {
        public void construct(Builder builder) {
        	builder.buildPartA();
            builder.buildPartB();
            builder.buildPartC();
        }
    }
}
