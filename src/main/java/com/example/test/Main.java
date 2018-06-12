package com.example.test;

import com.example.test.data.DtoFactory;
import com.example.test.sql.SqlGenerator;
import com.example.test.utils.DtoAccessor;
import com.example.test.utils.StringUtils;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    DtoFactory<User> factory = new DtoFactory<>(User.class);
    factory.addVariation("name", "Tom");
    factory.addVariation("name", "John");
    factory.addVariation("name", "Alex");
    factory.addVariation("name", "Jim");
    factory.addVariation("name", "Jenny");
    factory.addVariation("address", "Tokyo");
    factory.addVariation("address", "Osaka");
    factory.addVariation("address", "Fukuoka");
    factory.addVariation("address", "Kyoto");
    factory.addVariation("age", "21");
    factory.addVariation("age", "22");
    factory.addVariation("age", "23");
    factory.addVariation("age", "24");
    factory.addVariation("count", 1);
    factory.addVariation("count", 2);
    factory.addVariation("myString", "String1");
    factory.addVariation("myString", "String2");
    factory.addVariation("myString", "String3");

    List<User> users = factory.create(10);
    String[] names = DtoAccessor.getPropertyNames(User.class);

    System.out.println("-----CSV-----");
    System.out.println(String.join(",", StringUtils.camelToSnake(names, true))); // header
    for(User user : users) {
      System.out.println(DtoAccessor.toCsvString(user)); // values
    }

    System.out.println();

    System.out.println("-----SQL-----");
    for(User user : users) {
      System.out.println(SqlGenerator.toSql("USERS", user));
    }
  }
}
