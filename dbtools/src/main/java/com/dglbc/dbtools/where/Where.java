package com.dglbc.dbtools.where;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.unit.WKUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Supplier;

@Accessors(chain = true)
@Setter
@Getter
public class Where implements Serializable {


    private String logic;
    private StringBuilder sql;
    private List parms;
    private List<Where> conditions = new ArrayList<>();

    public Expression builder() {
        StringBuilder nsql = new StringBuilder();
        if (conditions.size() == 0) {
            if (null == logic) {
                logic = SQLKey.AND;
            }
            nsql.append(logic).append(sql);
        } else {
            nsql.append(logic).append(SQLKey.LEFT).append(sql);
            for (Where where : conditions) {
                Expression temp = where.builder();
                nsql.append(temp.getSql());
                parms.addAll(temp.getValues());
            }
            nsql.append(SQLKey.RIGHT);
        }
        return new Expression(nsql, parms);
    }

    public Where() {
        this.logic = SQLKey.AND;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
    }

    public Where(String logic) {
        this.logic = logic;
        this.sql = new StringBuilder();
        this.parms = new ArrayList();
    }

    public Where(Supplier<Expression> s) {
        this.logic = SQLKey.AND;
        caulse(s.get());
    }

    public Where(String logic, Supplier<Expression> s) {
        this.logic = logic;
        caulse(s.get());
    }

    //用java8的特性
    public Where caulse(Supplier<Expression> s) {
        caulse(s.get());
        return this;
    }

    // 运算
    public Where caulse(Expression expression) {
        sql.append(expression.getSql());
        parms.addAll(expression.getValues());
        return this;
    }

    public Where caulse(Table table, String name, String operation , List values){
        //根据情况 例如between 和in是要提前知道参数个数。所以先处理operation语句

        return this;
    }

    //where 1=1 and name ='My'

    //eq
    public Where eq(Table table, String name, Object object) {
        caulse(() -> {
            return new Expression(WKUnit.getWS(table, name,"= ? "), Arrays.asList(object));
        });
        return this;
    }


    //解决条件需要的多个组合or and
    public Where or(Where where) {
        conditions.add(where);
        return this;
    }
}
