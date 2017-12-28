package com.dglbc.dbtools.where;

import com.dglbc.dbtools.Statement;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.join.Join;
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

    private static String LEFT = " ( ";
    private static String RIGHT = " ) ";
    private String logic;
    private StringBuilder sql;
    private List parms;
    private List<Where> conditions=new ArrayList<>();

    public Statement builder() {
        Statement statement = null;
        StringBuilder nsql = new StringBuilder();
        if (conditions.size() ==0){
            nsql.append(logic).append(sql);
        }else {
            nsql.append(logic).append(LEFT).append(sql);
            for (Where where : conditions) {
                Statement temp = where.builder();
                nsql.append(temp.getSql());
                parms.addAll(temp.getValues());
            }
            nsql.append(RIGHT);
        }
        
        return new Statement(nsql.toString(), parms);
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
    public Where add(String name, Object value, String opt) {
        sql.append("A.").append(name).append(opt).append("? ");
        parms.add(value);
        return this;
    }

    // 运算
    public Where add(Join join, String name, Object value, String opt) {
        sql.append(join.getAlias()).append(".").append(name).append(opt).append("? ");
        parms.add(value);
        return this;
    }

    // 运算
    public Where add(String name, String opt) {
        sql.append("A.").append(name).append(opt);
        return this;
    }

    // 运算
    public Where add(Join join, String name, String opt) {
        sql.append(join.getAlias()).append(".").append(name).append(opt);
        return this;
    }

    // like
    public Where like(String name, String value) {
        return add(name, value, " LIKE ");
    }

    // not like
    public Where notLike(String name, String value) {
        return add(name, value, " NOT LIKE ");
    }


    // 大于
    public Where gt(String name, int value) {
        return add(name, value, ">");
    }

    // 大于等于
    public Where ge(String name, int value) {
        return add(name, value, " >= ");
    }

    // 小于
    public Where lt(String name, int value) {
        return add(name, value, " < ");
    }

    // 小于等于
    public Where le(String name, int value) {
        return add(name, value, " <= ");
    }

    // 等于
    public Where eq(String name, Object value) {
        return add(name, value, " = ");
    }

    // 不等于
    public Where neq(String name, Object value) {
        return add(name, value, " <> ");
    }

    // 为空
    public Where isNull(String name) {
        return add(name, " IS NULL ");
    }

    // 为空
    public Where isNotNull(String name) {
        return add(name, " IS NOT NULL ");
    }

    //between
    public Where between(String name, Object value, Object value1) {
        sql.append("A.").append(name).append(SqlKey.BETWEEN).append("?").append(SqlKey.AND).append("?");
        parms.add(value);
        parms.add(value1);
        return this;
    }

    //in
    public Where in(String name, List values) {
        sql.append("A.").append(name).append(SqlKey.IN);
        String temp = new String();
        for (Object o : values) {
            temp += ",?";
        }
        temp.replaceFirst(",", LEFT);
        sql.append(temp).append(RIGHT);
        parms.addAll(values);
        return this;
    }

    public Where in(String name, Object[] values) {
        return in(name, Arrays.asList(values));
    }


    // like
    public Where like(Join join, String name, String value) {
        return add(join, name, value, " LIKE ");
    }

    // not like
    public Where notLike(Join join, String name, String value) {
        return add(join, name, value, "NOT LIKE ");
    }


    // 大于
    public Where gt(Join join, String name, int value) {
        return add(join, name, value, ">");
    }

    // 大于等于
    public Where ge(Join join, String name, int value) {
        return add(join, name, value, " >= ");
    }

    // 小于
    public Where lt(Join join, String name, int value) {
        return add(join, name, value, " < ");
    }

    // 小于等于
    public Where le(Join join, String name, int value) {
        return add(join, name, value, " <= ");
    }

    // 等于
    public Where eq(Join join, String name, Object value) {
        return add(join, name, value, " = ");
    }

    // 不等于
    public Where neq(Join join, String name, Object value) {
        return add(join, name, value, " <> ");
    }

    // 为空
    public Where isNull(Join join, String name) {
        return add(join, name, " IS NULL ");
    }

    // 为空
    public Where isNotNull(Join join, String name) {
        return add(join, name, " IS NOT NULL ");
    }

    //between
    public Where between(Join join, String name, Object value, Object value1) {
        sql.append(join.getAlias()).append(".").append(name).append(SqlKey.BETWEEN).append("?").append(SqlKey.AND).append("?");
        parms.add(value);
        parms.add(value1);
        return this;
    }

    //in
    public Where in(Join join, String name, List values) {
        sql.append(join.getAlias()).append(".").append(name).append(SqlKey.IN);
        String temp = new String();
        for (Object o : values) {
            temp += ",?";
        }
        temp.replaceFirst(",", LEFT);
        sql.append(temp).append(RIGHT);
        parms.addAll(values);
        return this;
    }

    public Where in(Join join, String name, Object[] values) {
        return in(join, name, Arrays.asList(values));
    }

}
