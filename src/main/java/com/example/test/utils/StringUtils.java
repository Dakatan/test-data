package com.example.test.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

  public static String camelToSnake(String str) {
    return camelToSnake(str, true);
  }

  public static String camelToSnake(String str, boolean uppercase) {
    String convertedStr = str
            .replaceAll("([A-Z]+)([A-Z][a-z])", "$1_$2")
            .replaceAll("([a-z])([A-Z])", "$1_$2");
    return uppercase ? convertedStr.toUpperCase() : convertedStr.toLowerCase();
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
}
