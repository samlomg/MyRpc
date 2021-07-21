package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.annotation.Ignore;
import com.dglbc.dbassistant.annotation.MyColumn;
import com.dglbc.dbassistant.annotation.MyId;
import com.dglbc.dbassistant.annotation.MyTable;
import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
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

    public Update(Class cl, Object o) {
        //table
        if (cl.isAnnotationPresent(MyTable.class)) {
            MyTable myTable = (MyTable) cl.getAnnotation(MyTable.class);
            this.table = myTable.tableName();
        } else {
            this.table = cl.getSimpleName();
        }
        Field[] fields = cl.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(Ignore.class)) {
                continue;
            }
            if (field.isAnnotationPresent(MyId.class)) {
                continue;
            }
            try {
                if (field.isAnnotationPresent(MyColumn.class)) {
                    MyColumn myColumn = field.getAnnotation(MyColumn.class);
                    set(myColumn.columnName(), field.get(o));
                } else {
                    set(field.getName(), field.get(o));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
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

    public Update set(Column column) {
        if (this.columns == null) this.columns = Column.create();
        this.columns.set(column);
        return set();
    }

    public Update set(Express column) {
        if (this.columns == null) this.columns = Column.create();
        this.columns.columns().add(column);
        return set();
    }

    public Update set(String column, Object... value) {
        return set(new Express(column + " = ? ", value == null || value.length == 0 ? null : Arrays.asList(value)));
    }

    public static Update TABLE(String table) {
        return new Update(table);
    }

    public static <T> Update TABLE(T t){
        return new Update(t.getClass(),t);
    }

}
