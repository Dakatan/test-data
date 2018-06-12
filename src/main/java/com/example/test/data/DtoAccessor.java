package com.example.test.data;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

public class DtoAccessor {

  public static void set(String key, Object value, Object dest) {
    BeanInfo beanInfo = getBeanInfo(dest.getClass());
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);

    for(PropertyDescriptor property : properties) {
      if(key.equals(property.getName())) {
        invokeInternal(dest, property.getWriteMethod(), value);
      }
    }
  }

  public static Object get(String key, Object target) {
    BeanInfo beanInfo = getBeanInfo(target.getClass());
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);

    for(PropertyDescriptor property : properties) {
      if(key.equals(property.getName())) {
        return invokeInternal(target, property.getReadMethod());
      }
    }
    return null;
  }

  public static String toCsvString(Object target) {
    return toCsvString(target, ",");
  }

  public static String toCsvString(Object target, String delm) {
    StringBuilder sb = new StringBuilder();
    String[] propertyNames = getPropertyNames(target.getClass());

    List<String> list = new LinkedList<>();
    for(String propertyName : propertyNames) {
      Object obj = get(propertyName, target);
      String value;
      if(obj == null) {
        value = "";
      } else if(obj instanceof String) {
        value = (String) obj;
      } else if(obj instanceof Number || obj instanceof Boolean) {
        value = String.valueOf(obj);
      } else {
        value = obj.toString();
      }
      list.add(value);
    }

    sb.append(String.join(delm, list.toArray(new String[list.size()])));
    return sb.toString();
  }

  public static String[] getPropertyNames(Class<?> clazz) {
    BeanInfo beanInfo = getBeanInfo(clazz);
    PropertyDescriptor properties[] = getReadableWritableProperties(beanInfo);
    String[] result = new String[properties.length];
    for(int i = 0; i < properties.length; i++) {
      PropertyDescriptor property = properties[i];
      result[i] = property.getName();
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
