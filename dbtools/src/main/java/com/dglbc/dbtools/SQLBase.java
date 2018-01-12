package com.dglbc.dbtools;

import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;

/**
 * Created by LBC on 2018/1/11
 **/

public class SQLBase {

    //表达式
    public static Expression expression(Column column, boolean flag) {
        return new Expression(column, flag);
    }

    //true 是语句生成的表，false是原生表
    public static Expression expression(Table table, boolean flag) {
        return new Expression(table, flag);
    }

    public static Expression expression(String sql) {
        return new Expression(sql);
    }

    //Column
    public static Column column() {
        return new Column();
    }

    public static Column column(Table table, String name) {
        return new Column(table, name);
    }

    public static Column column(Table table, String name, Object value) {
        return new Column(table, name, value);
    }

    //table
    public static Table table(String name, String alias) {
        return new Table(name, alias);
    }

    public static Table table(Expression expression, String alias) {
        return new Table(expression, alias);
    }

    public static Table table() {
        return new Table();
    }

    //where
    public static Where where(String logic) {
        return new Where(logic);
    }

    public static Where where() {
        return new Where();
    }

    //JOIN
    public static Join join(String join, Expression table) {
        return new Join(join, table);
    }

    public static Join join(String join, Table table) {
        return new Join(join, table);
    }

    public static Join join(String join, Table table, Column column, Column column2) {
        return new Join(join, table, column, column2);
    }


}
