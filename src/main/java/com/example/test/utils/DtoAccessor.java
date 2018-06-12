package com.example.test.utils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
    return toCsvString(target, true);
  }

  public static String toCsvString(Object target, boolean hasHeader) {
    final String separator = System.lineSeparator();
    final String comma = ",";
    StringBuilder sb = new StringBuilder();
    String[] columns = getColumns(target);
    if(hasHeader) sb.append(String.join(comma, columns)).append(separator);
    List<String> list = new LinkedList<>();
    for(String column : columns) {
      list.add(get(column, target));
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
      result[i] = toSnakeCase ? camelToSnake(property.getName()) : property.getName();
    }
    return result;
  }

  public static String camelToSnake(String str) {
    String convertedStr = str
            .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
            .replaceAll("([a-z])([A-Z])", "$1_$2");
    return convertedStr.toLowerCase();
  }

  public static String snakeToCamel(String str) {
    Pattern p = Pattern.compile("_([a-z])");
    Matcher m = p.matcher(str.toLowerCase());

    StringBuffer sb = new StringBuffer(str.length());
    while (m.find()) {
      m.appendReplacement(sb, m.group(1).toUpperCase());
    }

    m.appendTail(sb);
    return sb.toString();
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
