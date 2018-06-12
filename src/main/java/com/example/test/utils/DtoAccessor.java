package com.example.test.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class DtoAccessor {

  public static void set(String key, String value, Object dest) {
    BeanInfo beanInfo = getBeanInfo(dest.getClass());
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);
    for(PropertyDescriptor property : properties) {
      if(key.equals(property.getName())) {
        invokeInternal(dest, property.getWriteMethod(), value);
      }
    }
  }

  public static String get(String key, Object target) {
    BeanInfo beanInfo = getBeanInfo(target.getClass());
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);
    for(PropertyDescriptor property : properties) {
      if(key.equals(property.getName())) {
        return (String) invokeInternal(target, property.getReadMethod());
      }
    }
    return null;
  }

  public static String toCsvString(Object target) {
    final String comma = ",";
    StringBuilder sb = new StringBuilder();
    String[] columns = getColumns(target);
    List<String> list = new LinkedList<>();
    for(String column : columns) {
      String value = get(column, target);
      if(value == null) value = "";
      list.add(value);
    }
    sb.append(String.join(comma, list.toArray(new String[list.size()])));
    return sb.toString();
  }

  public static String[] getColumns(Object target) {
    return getColumns(target, false);
  }

  public static String[] getColumns(Object target, boolean toSnakeCase) {
    BeanInfo beanInfo = getBeanInfo(target.getClass());
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);
    String[] result = new String[properties.length];
    for(int i = 0; i < properties.length; i++) {
      PropertyDescriptor property = properties[i];
      result[i] = toSnakeCase ? StringUtils.camelToSnake(property.getName()) : property.getName();
    }
    return result;
  }

  private static BeanInfo getBeanInfo(Class<?> clazz) {
    try {
      return Introspector.getBeanInfo(clazz);
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static PropertyDescriptor[] getReadableWritableProperties(BeanInfo beanInfo) {
    PropertyDescriptor[] properties = beanInfo.getPropertyDescriptors();
    List<PropertyDescriptor> list = new LinkedList<>();
    for(PropertyDescriptor property : properties) {
      if(property.getWriteMethod() != null && property.getReadMethod() != null) {
        list.add(property);
      }
    }
    return list.toArray(new PropertyDescriptor[list.size()]);
  }

  private static Object invokeInternal(Object target, Method method, Object... args) {
    try {
      method.setAccessible(true);
      if(args != null) {
        return method.invoke(target, args);
      } else {
        return method.invoke(target);
      }
    } catch (IllegalAccessException | InvocationTargetException e) {
      throw new RuntimeException(e);
    }
  }
}
