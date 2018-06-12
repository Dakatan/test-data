package com.example.test.data;

import com.example.test.utils.DtoAccessor;

import java.util.*;

public class DtoFactory<T> {
  private final Class<T> clazz;
  private final Random random = new Random();
  private Map<String, List<String>> dataVariations;

  public DtoFactory(Class<T> clazz) {
    this.clazz = clazz;
    dataVariations = new HashMap<>();
  }

  public List<T> create(final int size) {
    List<T> result = new ArrayList<>();
    for(int i = 0; i < size; i++) {
      result.add(create());
    }
    return result;
  }

  public T create() {
    T dto = createInternal();
    for(Map.Entry<String, List<String>> entry : dataVariations.entrySet()) {
      if(entry.getValue().size() != 0) {
        int index = random.nextInt(entry.getValue().size());
        DtoAccessor.set(entry.getKey(), entry.getValue().get(index), dto);
      }
    }
    return dto;
  }

  public void addVariation(String key, List<String> value) {
    dataVariations.put(key, value);
  }

  public void addVariation(String key, String[] value) {
    dataVariations.put(key, Arrays.asList(value));
  }

  public void removeVariation(String key) {
    dataVariations.remove(key);
  }

  private T createInternal() {
    T result;
    try {
      result = clazz.newInstance();
    } catch (InstantiationException | IllegalAccessException e) {
      throw new RuntimeException(e);
    }
    return result;
  }

}
