package com.dglbc.dbtools.assistant;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;

import java.util.Map;

public class Select {
    private Table table;
    private Map<String,Expression> content;//查询的名称
    private Expression special;
    private Map<String,Where> conditions;
    private Map<String,Join> joins;
    private boolean order = false;
    private boolean group = false;
    private boolean have = false;
    private Map<String,Column> groupContent ;
    private Map<String,Column> orderContent ;
    private Map<String,Where> havingConditions ;


}
