package com.example.test;

import com.example.test.data.DtoFactory;
import com.example.test.utils.DtoAccessor;

import java.util.List;

public class Main {
  public static void main(String[] args) {
    DtoFactory<User> factory = new DtoFactory<>(User.class);
    factory.addVariation("name", "Tom");
    factory.addVariation("address", "Tokyo");
    factory.addVariation("age", "21");
    List<User> users = factory.create(10);
    for(User user : users) System.out.println(DtoAccessor.toCsvString(user));
  }
}
