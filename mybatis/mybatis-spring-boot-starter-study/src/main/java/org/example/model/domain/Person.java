package org.example.model.domain;

import java.util.List;
import lombok.Data;

@Data
public class Person {

  private String name;
  private Contact contact;
  private List<String> tags;
  private Level1 level1;
  private List<Item> items;

  // Contact 内部类
  @Data
  public static class Contact {

    private String address;
    private String phone;
  }

  // Level1 内部类
  @Data
  public static class Level1 {

    private Level2 level2;
  }

  // Level2 内部类
  @Data
  public static class Level2 {

    private String value;
  }

  // Item 内部类
  @Data
  public static class Item {

    private String name;
    private int price;
  }
}
