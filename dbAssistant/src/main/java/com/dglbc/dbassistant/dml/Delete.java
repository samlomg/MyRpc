package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;


/*
 * 目前只作单表更新的操作
 *delete from table where a=2
 * */

@Accessors(fluent = true)
@Setter
@Getter
@ToString
@NoArgsConstructor
public class Delete extends Condition<Delete> implements DML {

    private String table;//主表有查询语句必有主表查询的

    public Express build() {
        //build
        if (sec()) clear();
        this.sql().append(K.DELETE).append(K.FROM).append(table);
        checkParts(wheres());
        //重要一点如果是已经构建生成的express必须有标志
        this.sec(true);
        return this;
    }

    public static Delete from(String table) {
        return new Delete(table);
    }

    public Delete(String table) {
        this.table = table;
    }


    @Override
    public Delete me() {
        return this;
    }
}
