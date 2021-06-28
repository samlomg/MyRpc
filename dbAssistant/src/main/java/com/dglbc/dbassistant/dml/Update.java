package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * 20201014决定只做单表更新
 * update A set ab=? where id =1
 */
@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Update extends Condition<Update> implements  DML {

    private boolean isNormal = false;

    /**
     * 100%的查询语句是带查询的列名表名
     * 基础都用express会提供转化的方法
     */
    private Column columns;

    private String table;//主表有查询语句必有主表查询的


    public Express build() {
        //初始化
        if (sec()) clear();
        this.sql().append(K.UPDATE).append(table);
        if (isNormal) this.sql().append(K.SET);
        checkParts(columns);
        checkParts(wheres());
        //重要一点如果是已经构建生成的express必须有标志
        this.sec(true);
        return this;
    }
    @Override
    public Update me() {
        return this;
    }
    //先来最多的情况
    public Update(String table, String col, String where) {
        this.isNormal = true;
        this.columns = new Column(new Express(col));
        this.table = table;
        //todo 目前没想到什么好的办法只能先new然后赋值
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres(new Where(specialExpress));
    }

    //补充情况
    public Update(String table, String where) {
        this.table = table;
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres(new Where(specialExpress));
    }

    public Update(String sqlAll) {
        this.table = sqlAll;
    }

    public Update set() {
        this.isNormal = true;
        return this;
    }

    public Update set(Express column) {
        if (this.columns == null) this.columns = new Column();
        this.isNormal = true;
        this.columns.columns().add(column);
        return this;
    }

    public Update set(String column, Object... value) {
        if (this.columns == null) this.columns = new Column();
        this.isNormal = true;
        this.columns.columns().add(new Express(column + " = ? ", value == null || value.length == 0 ? null : Arrays.asList(value)));
        return this;
    }

    public static Update TABLE(String table) {
        return new Update(table);
    }

}
