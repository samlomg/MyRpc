package com.dglbc.dbassistant.dml;


import com.dglbc.dbassistant.base.DML;
import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.base.K;
import com.dglbc.dbassistant.declare.Er;
import com.dglbc.dbassistant.tips.TipsShow;
import lombok.*;
import lombok.experimental.Accessors;

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


    public Insert(String table) {
        this.table = table;
    }

    public static Insert into(String table) {
        return new Insert(table);
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
