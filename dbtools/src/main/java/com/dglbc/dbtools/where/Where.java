package com.dglbc.dbtools.where;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
public class Where implements Serializable {


    private String logic;
    private StringBuilder sql;
    private List parms;
    private List<Where> conditions = new ArrayList<>();

    public Expression builder() {
        StringBuilder nsql = new StringBuilder();
        if (conditions.size() == 0) {
            if (null == logic){
                logic = SQLKey.AND;
            }
            nsql.append(logic).append(sql);
        } else {
            nsql.append(logic).append(SQLKey.LEFT).append(sql);
            for (Where where : conditions) {
                Expression temp = where.builder();
                nsql.append(temp.getSql());
                parms.addAll(temp.getValues());
            }
            nsql.append(SQLKey.RIGHT);
        }

        return new Expression(nsql, parms);
    }

    public Where(String logic) {
        this.logic = logic;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
    }

    public Where() {
        this.logic = SQLKey.AND;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
    }

    //增加一个有争议的构造函数。因为这个要看使用人使用是否妥当。
    public Where(StringBuilder sql, Object... parms) {
        this.sql = sql;
        this.parms = Arrays.asList(parms);
    }

    public Where(StringBuilder sql, String... parms) {
        this.sql = sql;
        this.parms = Arrays.asList(parms);
    }
    public Where(StringBuilder sql, Integer... parms) {
        this.sql = sql;
        this.parms = Arrays.asList(parms);
    }
    public Where(StringBuilder sql, Double... parms) {
        this.sql = sql;
        this.parms = Arrays.asList(parms);
    }
    public Where(StringBuilder sql, Float... parms) {
        this.sql = sql;
        this.parms = Arrays.asList(parms);
    }

    public Where or(Where where) {
        conditions.add(where);
        return this;
    }

    public Where and(Where where) {
        conditions.add(where);
        return this;
    }

    // 运算
    public Where add(Expression expression, Object value, String opt) {
        sql.append(expression.getSql()).append(opt).append(" ? ");
        parms.addAll(expression.getValues());
        parms.add(value);
        return this;
    }

    // 运算
    public Where add(String caulse) {
        sql.append(caulse);
        return this;
    }

    // 运算
    public Where add(Expression expression) {
        sql.append(expression.getSql());
        parms.addAll(expression.getValues());
        return this;
    }


    // like
    public Where like(Column column) {
        return like(new Expression(column, false), column.getValue());
    }

    // not like
    public Where notLike(Column column) {
        return notLike(new Expression(column, false), column.getValue());
    }

    // 大于
    public Where gt(Column column) {
        return gt(new Expression(column, false), column.getValue());
    }

    // 大于等于
    public Where ge(Column column) {
        return ge(new Expression(column, false), column.getValue());
    }

    // 小于
    public Where lt(Column column) {
        return lt(new Expression(column, false), column.getValue());
    }

    // 小于等于
    public Where le(Column column) {
        return le(new Expression(column, false), column.getValue());
    }

    // 等于
    public Where eq(Column column) {
        return eq(new Expression(column, false), column.getValue());
    }

    // 不等于
    public Where neq(Column column) {
        return neq(new Expression(column, false), column.getValue());
    }

    // 为空
    public Where isNull(Column column) {
        return isNull(new Expression(column, false));
    }

    // 为空
    public Where isNotNull(Column column) {
        return isNotNull(new Expression(column, false));
    }

    //between
    public Where between(Column column, Object value, Object value1) {
        return between(new Expression(column, false), value, value1);
    }

    //in
    public Where in(Column column, List values) {
        return in(new Expression(column, false), values);
    }


    //第二种形式
    // 大于

    // like
    public Where like(Expression expression, Object value) {
        return add(expression, value, " LIKE ");
    }

    // not like
    public Where notLike(Expression expression, Object value) {
        return add(expression, value, " NOT LIKE ");
    }

    // 大于
    public Where gt(Expression expression, Object value) {
        return add(expression, value, ">");
    }

    // 大于等于
    public Where ge(Expression expression, Object value) {
        return add(expression, value, " >= ");
    }

    // 小于
    public Where lt(Expression expression, Object value) {
        return add(expression, value, " < ");
    }

    // 小于等于
    public Where le(Expression expression, Object value) {
        return add(expression, value, " <= ");
    }

    // 等于
    public Where eq(Expression expression, Object value) {
        return add(expression, value, " = ");
    }

    // 不等于
    public Where neq(Expression expression, Object value) {
        return add(expression, value, " <> ");
    }

    // 为空
    public Where isNull(Expression expression) {
        expression.setSql(expression.getSql().append(" IS NULL "));
        return add(expression);
    }

    // 为空
    public Where isNotNull(Expression expression) {
        expression.setSql(expression.getSql().append(" IS NOT NULL "));
        return add(expression);
    }

    //between
    public Where between(Expression expression, Object value, Object value1) {
        sql.append(expression.getSql()).append(SQLKey.BETWEEN).append("?").append(SQLKey.AND).append("?");
        parms.addAll(expression.getValues());
        parms.add(value);
        parms.add(value1);
        return this;
    }

    //in
    public Where in(Expression expression, List values) {
        sql.append(expression.getSql()).append(SQLKey.IN);
        String temp = new String();
        for (Object o : values) {
            temp += ",?";
        }
        temp.replaceFirst(",", SQLKey.LEFT);
        sql.append(temp).append(SQLKey.RIGHT);
        parms.addAll(values);
        return this;
    }

    //第3种形式
    // 等于


    // like
    public Where like(Table table, String name, Object values) {
        return like(new Column(table, name, values));
    }

    // not like
    public Where notLike(Table table, String name, Object values) {
        return notLike(new Column(table, name, values));
    }

    // 大于
    public Where gt(Table table, String name, Object values) {
        return gt(new Column(table, name, values));
    }

    // 大于等于
    public Where ge(Table table, String name, Object values) {
        return ge(new Column(table, name, values));
    }

    // 小于
    public Where lt(Table table, String name, Object values) {
        return lt(new Column(table, name, values));
    }

    // 小于等于
    public Where le(Table table, String name, Object values) {
        return le(new Column(table, name, values));
    }

    // 等于
    public Where eq(Table table, String name, Object values) {
        return eq(new Column(table, name, values));
    }

    // 不等于
    public Where neq(Table table, String name, Object values) {
        return neq(new Column(table, name, values));
    }

    // 为空
    public Where isNull(Table table, String name) {
        return isNull(new Column(table, name));
    }

    // 为空
    public Where isNotNull(Table table, String name) {
        return isNotNull(new Column(table, name));
    }

    //between
    public Where between(Table table, String name, Object value, Object value1) {
        return between(new Column(table, name), value, value1);
    }

    //in
    public Where in(Table table, String name, List values) {
        return in(new Column(table, name), values);
    }
}
