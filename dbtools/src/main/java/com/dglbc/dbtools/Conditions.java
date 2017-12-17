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
    public Conditions gt(String clause, int value) {
        return appendArithmetic(clause, value, ">");

    }

    // 添加整数相等约束
    public Conditions ge(String clause, int value) {
        return appendArithmetic(clause, value, ">=");
    }

    // 添加整数相等约束
    public Conditions lt(String clause, int value) {
        return appendArithmetic(clause, value, "<");
    }

    // 添加整数相等约束
    public Conditions le(String clause, int value) {
        return appendArithmetic(clause, value, "<=");
    }

    // 添加整数相等约束
    public Conditions eq(String clause, int value) {
        return appendArithmetic(clause, value, "=");
    }


    // 运算
    public Conditions appendArithmetic(String clause, int value, String opt) {
        return new Conditions(opt, name, value);

    }

}
