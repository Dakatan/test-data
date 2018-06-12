package com.example.test.utils;

public class StringUtils {

  public static String[] camelToSnake(String[] camels) {
    return camelToSnake(camels, false);
  }

  public static String[] camelToSnake(String[] camels, boolean uppercase) {
    String[] result = new String[camels.length];
    for(int i = 0; i < camels.length; i++) {
      result[i] = camelToSnake(camels[i], uppercase);
    }
    return result;
  }

  public static String camelToSnake(String camel) {
    return camelToSnake(camel, false);
  }

  public static String camelToSnake(String camel, boolean uppercase) {
    final StringBuilder sb = new StringBuilder(camel.length() + camel.length());
    for (int i = 0; i < camel.length(); i++) {
      final char c = camel.charAt(i);
      if (Character.isUpperCase(c)) {
        sb.append(sb.length() != 0 ? '_' : "").append(Character.toLowerCase(c));
      } else {
        sb.append(Character.toLowerCase(c));
      }
    }
    return uppercase ? sb.toString().toUpperCase() : sb.toString().toLowerCase();
  }

  public static String[] snakeToCamel(String[] snakes) {
    String[] result = new String[snakes.length];
    for(int i = 0; i < snakes.length; i++) {
      result[i] = snakeToCamel(snakes[i]);
    }
    return result;
  }

  public static String snakeToCamel(String snake) {
    final StringBuilder sb = new StringBuilder(snake.length() + snake.length());
    for (int i = 0; i < snake.length(); i++) {
      final char c = snake.charAt(i);
      if (c == '_') {
        sb.append((i + 1) < snake.length() ? Character.toUpperCase(snake.charAt(++i)) : "");
      } else {
        sb.append(sb.length() == 0 ? Character.toUpperCase(c) : Character.toLowerCase(c));
      }
    }
    return sb.toString();
  }
}
