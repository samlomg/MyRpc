package com.dglbc.dbtools.where;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.table.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
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
            nsql.append(logic).append(sql);
        } else {
            nsql.append(logic).append(SqlKey.LEFT).append(sql);
            for (Where where : conditions) {
                Expression temp = where.builder();
                nsql.append(temp.getSql());
                parms.addAll(temp.getValues());
            }
            nsql.append(SqlKey.RIGHT);
        }

        return new Expression(nsql, parms);
    }

    public Where(String logic) {
        this.logic = logic;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
    }

    public Where() {
        this.logic = SqlKey.AND;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
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

    // like
    public Where like(Column column) {
        return add(new Expression(column, false), column.getValue(), " LIKE ");
    }

    // not like
    public Where notLike(Column column) {
        return add(new Expression(column, false), column.getValue(), " NOT LIKE ");
    }

    // 大于
    public Where gt(Column column) {
        return add(new Expression(column, false), column.getValue(), ">");
    }

    // 大于等于
    public Where ge(Column column) {
        return add(new Expression(column, false), column.getValue(), " >= ");
    }

    // 小于
    public Where lt(Column column) {
        return add(new Expression(column, false), column.getValue(), " < ");
    }

    // 小于等于
    public Where le(Column column) {
        return add(new Expression(column, false), column.getValue(), " <= ");
    }

    // 等于
    public Where eq(Column column) {
        return add(new Expression(column, false), column.getValue(), " = ");
    }

    // 不等于
    public Where neq(Column column) {
        return add(new Expression(column, false), column.getValue(), " <> ");
    }

    // 为空
    public Where isNull(Column column) {
        return add(column.getTable().getAlias() + "." + column.getName() + " IS NULL ");
    }

    // 为空
    public Where isNotNull(Column column) {
        return add(column.getTable().getAlias() + "." + column.getName() + " IS NOT NULL ");
    }

    //between
    public Where between(Column column, Object value, Object value1) {
        return between(new Expression(column, false), value, value1);
    }

    //between
    public Where between(Expression expression, Object value, Object value1) {
        sql.append(expression.getSql()).append(SqlKey.BETWEEN).append("?").append(SqlKey.AND).append("?");
        parms.addAll(expression.getValues());
        parms.add(value);
        parms.add(value1);
        return this;
    }

    //in
    public Where in(Column column, List values) {
        return in(new Expression(column, false), values);
    }

    //in
    public Where in(Expression expression, List values) {
        sql.append(expression.getSql()).append(SqlKey.IN);
        String temp = new String();
        for (Object o : values) {
            temp += ",?";
        }
        temp.replaceFirst(",", SqlKey.LEFT);
        sql.append(temp).append(SqlKey.RIGHT);
        parms.addAll(values);
        return this;
    }

    // 大于
    public Where gt(Expression expression,Object value) {
        return add(expression, value, ">");
    }

    // 等于
    public Where eq(Table table,String name,Object values) {
        return eq(new Column(table,name,values));
    }
}
