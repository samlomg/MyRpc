package com.dglbc.dbtools.table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * Created by LBC on 2017/12/28
 **/

@Accessors(chain = true)
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Column implements Serializable{
    private  Table table;
    private String name;
    private Object value;
    private String bind;

    public Column(Table table, String name) {
        this.table = table;
        this.name = name;
        table.getColumns().add(this);
    }

}
