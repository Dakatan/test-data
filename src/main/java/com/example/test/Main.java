package com.example.test;

import com.example.test.data.DtoFactory;
import com.example.test.utils.DtoAccessor;

public class Main {
  public static void main(String[] args) {
    DtoFactory<User> factory = new DtoFactory<>(User.class);
    factory.addVariation("name", new String[]{"Tom", "John"});
    factory.addVariation("address", new String[]{"Tokyo"});
    factory.addVariation("age", new String[]{"21"});
    User user = factory.create();
    System.out.println(DtoAccessor.toCsvString(user));
  }
}
