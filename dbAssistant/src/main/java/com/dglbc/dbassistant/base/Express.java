package com.dglbc.dbassistant.base;

import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/29
 */

@Accessors(fluent = true)
@Setter
@Getter
@EqualsAndHashCode
public  class Express  {
    private StringBuilder sql=new StringBuilder();
    private List<Object> values;

    private boolean sec = false;//是否是已经生成查询sql语句,默认是否

    public Express(StringBuilder sql, List<Object> values) {
        this.sql .append(sql);
        this.values.addAll(values);

    }

    public Express(StringBuilder sql, List<Object> values, boolean sec) {
        this.sql.append(sql);
        this.values = new ArrayList<Object>(){{
            addAll(values);
        }};
        this.sec = sec;
    }

    public Express(String sql, List<Object> values, boolean sec) {
        this.sql.append(sql);
        this.values = new ArrayList<Object>(){{
            addAll(values);
        }};
        this.sec = sec;
    }

    public Express() {
        this.values = new ArrayList<>();
    }

    //合并在一起
    public Express merge(Express express) {
        this.sql.append(express.sql());
        this.values.addAll(express.values());
        return this;
    }

    //合并在一起
    public Express merge(String sql, List<Object> objects) {
        this.sql.append(sql);
        this.values.addAll(objects);
        return this;
    }

}
