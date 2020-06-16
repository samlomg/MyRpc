package com.dglbc.dbtools.unit;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class ClassGenSQL {
    Map<String, Express> selectContent = new HashMap<>();
    Table table;

    public ClassGenSQL(Class cl) {
        this.table =  new Table(cl.getSimpleName(), cl.getSimpleName() + "_lb");
        select(cl.getDeclaredFields());

    }

    public ClassGenSQL select(Field[] fields) {
        //首先获取class的属性 来生成sql语句
        for (Field field : fields) {
            this.selectContent.put(table.getName()+"-"+field.getName(), new Express(new Column(table,field.getName())));
        }
        return this;
    }

}
