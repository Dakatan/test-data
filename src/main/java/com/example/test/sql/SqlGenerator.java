package com.example.test.sql;

import com.example.test.utils.DtoAccessor;

public class SqlGenerator {
  public static String toSql(String tableName, Object object) {
    String[] columns = DtoAccessor.getColumns(object, true);
    StringBuilder sb = new StringBuilder();
    sb.append(String.join(", ", columns));
    String[] values = new String[columns.length];
    for(int i = 0; i < columns.length; i++) {
      String value = DtoAccessor.get(columns[i], object);
      values[i] = value == null ? "NULL" : value;
    }
    sb.append(String.join(", ", values));
    return sb.toString();
  }
}
