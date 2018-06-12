package com.example.test.sql;

import com.example.test.data.DtoAccessor;
import com.example.test.utils.StringUtils;

public class SqlGenerator {

  public static String toSql(String tableName, Object object) {
    StringBuilder sb = new StringBuilder();
    sb.append("INSERT INTO ").append(tableName.toUpperCase());

    String[] columns = DtoAccessor.getPropertyNames(object.getClass());
    sb.append(" (").append(String.join(", ", toUpper(columns))).append(") ");

    String[] values = new String[columns.length];
    for(int i = 0; i < columns.length; i++) {
      Object obj = DtoAccessor.get(columns[i], object);
      if(obj == null) {
        values[i] = "NULL";
      } else if(obj instanceof String) {
        values[i] = "\'" + obj + "\'";
      } else if(obj instanceof Number || obj instanceof Boolean) {
        values[i] = String.valueOf(obj);
      } else {
        values[i] = "\'" + obj.toString() + "\'";
      }
    }

    sb.append("VALUES (").append(String.join(", ", values)).append(");");
    return sb.toString();
  }

  private static String[] toUpper(String[] columns) {
    String[] result = new String[columns.length];
    for(int i = 0; i < columns.length; i++) {
      result[i] = StringUtils.camelToSnake(columns[i], true);
    }
    return result;
  }
}
