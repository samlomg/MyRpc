package com.dglbc.dbtools.table;

import com.dglbc.dbtools.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by LBC on 2017/12/28
 **/

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Table implements Serializable{
    private String name;
    private String alias;
    private String link;
    private String database;
    private String sec;//dbo
    private List<Column> columns;

    private StringBuilder sql;
    private List values;

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.columns = new ArrayList<>();
        Expression expression = new Expression(this, false);
        this.sql = expression.getSql();
        this.values = expression.getValues();

    }

    public Table(Expression expression,String alias) {
        this.sql = expression.getSql();
        this.values = expression.getValues();
        this.alias = alias;
        this.columns = new ArrayList<>();
    }
}
