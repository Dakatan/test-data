package com.example.test;

import com.example.test.data.DtoFactory;
import com.example.test.sql.SqlGenerator;
import com.example.test.utils.DtoAccessor;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    DtoFactory<User> factory = new DtoFactory<>(User.class);
    factory.addVariation("name", "Tom");
    factory.addVariation("address", "Tokyo");
    factory.addVariation("age", "21");
    factory.addVariation("count", 1);
    factory.addVariation("count", 2);
    factory.addVariation("myString", "String");
    List<User> users = factory.create(2);
    for(User user : users){
      System.out.println(DtoAccessor.toCsvString(user));
      System.out.println(SqlGenerator.toSql("USERS", user));
    }
  }
}
