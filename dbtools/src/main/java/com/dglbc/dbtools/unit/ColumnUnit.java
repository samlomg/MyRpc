package com.dglbc.dbtools.unit;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;

public class ColumnUnit {
    //获取表
    public static StringBuilder getColumn(Column column) {
        return new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
    }

    //获取表
    public static StringBuilder getColumn(Table table, String name) {
        return new StringBuilder().append(" ").append(table.getAlias()).append(".").append(name).append(" ");
    }
}
