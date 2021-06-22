package com.dglbc.dbtools.table;

import com.dglbc.dbtools.Express;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by LBC on 2017/12/28
 **/

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Table implements Serializable {
    private String name;
    private String alias;
    private String link;
    private String database;
    private String sec;//dbo
    private Map<String,Column> columns;

    private StringBuilder sql;
    private List values;

    public Table(String name, String alias) {
        this.name = name;
        this.alias = alias;
        this.columns = new HashMap<>();
        Express express = new Express(this, false);
        this.sql = express.getSql();
        this.values = express.getValues();

    }

    public Table(String name) {
        this.name = name;
        this.alias = alias;
        this.columns = new HashMap<>();
        Express express = new Express(this, false);
        this.sql = express.getSql();
        this.values = express.getValues();
    }

    public Table(Express express, String alias) {
        this.sql = express.getSql();
        this.values = express.getValues();
        this.alias = alias;
        this.columns = new HashMap<>();
    }
}