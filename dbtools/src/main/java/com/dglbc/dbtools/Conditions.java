package com.dglbc.dbtools;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Setter
@Getter
public class Conditions {
    private String operation;
    private String name;
    private Object value;

    public Conditions(String operation, String name, Object value) {
        this.operation = operation;
        this.name = name;
        this.value = value;
    }

    // 添加整数相等约束
    public static Conditions  gt(String name, int value) {
        return add(name, value, ">");

    }

    // 添加整数相等约束
    public static Conditions ge(String name, int value) {
        return add(name, value, ">=");
    }

    // 添加整数相等约束
    public static Conditions lt(String name, int value) {
        return add(name, value, "<");
    }

    // 添加整数相等约束
    public static Conditions le(String name, int value) {
        return add(name, value, "<=");
    }

    // 添加整数相等约束
    public static Conditions eq(String name, int value) {
        return add(name, value, "=");
    }


    // 运算
    public static Conditions add(String name, int value, String opt) {
        return new Conditions(opt, name, value);

    }

}
