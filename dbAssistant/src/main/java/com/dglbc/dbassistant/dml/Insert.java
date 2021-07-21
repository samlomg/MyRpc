package com.dglbc.dbassistant.dml;


import com.dglbc.dbassistant.annotation.Ignore;
import com.dglbc.dbassistant.annotation.MyColumn;
import com.dglbc.dbassistant.annotation.MyId;
import com.dglbc.dbassistant.annotation.MyTable;
import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Er;
import com.dglbc.dbassistant.tips.TipsShow;
import lombok.*;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.security.PrivateKey;
import java.util.ArrayList;
import java.util.List;

/**
 * insert into A (id,name) values (1,'lb')
 */
@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Insert extends Express implements DML {

    private String table;

    private List<Er<String, Object>> columns = new ArrayList<>();

    public Insert(Class cl, Object o) {
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
                    value(myColumn.columnName(), field.get(o));
                } else {
                    value(field.getName(), field.get(o));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }

    public Insert(String table) {
        this.table = table;
    }

    public static Insert into(String table) {
        return new Insert(table);
    }

    public static <T> Insert  into(T t) {
        return new Insert(t.getClass(),t);
    }

    public Insert value(String column, Object value) {
        columns().add(new Er<String, Object>(column, value));
        return this;
    }

    public Express build() {
        if (sec()) {
            clear();
        }
        if (columns.size() == 0) {
            TipsShow.alert("请输入要插入的参数");
        }

        StringBuilder sbForcolumn = new StringBuilder();
        StringBuilder sbForvalue = new StringBuilder();

        for (Er<String, Object> er : columns) {
            sbForcolumn.append(",").append(er.getFirst());
            sbForvalue.append(",?");
            values().add(er.getSecond());
        }
        this.sql().append(K.INSERT).append(table).append(" ( ").append(sbForcolumn.delete(0, 1))
                .append(" ) values ( ").append(sbForvalue.delete(0, 1)).append(" ) ");
        sec(true);
        return this;
    }

}
