package com.dglbc.dbtools.unit;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class ClassGenSQL {
    Map<String, Expression> selectContent = new HashMap<>();
    Table table;

    public ClassGenSQL(Class cl) {
        this.table =  new Table(cl.getSimpleName(), cl.getSimpleName() + "_lb");
        select(cl.getDeclaredFields());

    }

    public ClassGenSQL select(Field[] fields) {
        //首先获取class的属性 来生成sql语句
        for (Field field : fields) {
            this.selectContent.put(table.getName()+"-"+field.getName(), new Expression(new Column(table,field.getName())));
        }
        return this;
    }

}
