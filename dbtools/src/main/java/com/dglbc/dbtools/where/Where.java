package com.dglbc.dbtools.where;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.unit.ColumnUnit;
import com.dglbc.dbtools.unit.WKUnit;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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

    public Where caulse(Table table, String name, String operation, List values) {
        //根据情况 例如between 和in是要提前知道参数个数。所以先处理operation语句
        caulse(() -> {
            return new Expression(ColumnUnit.getColumn(table, name).append(WKUnit.getOperation(operation, values)), values);
        });
        return this;
    }

    public Where caulse(Column column, String operation, List values) {
        caulse(column.getTable(), column.getName(), operation, values);
        return this;
    }

    //基础到此结束

    //第一部分用table name 还有值
    //eq
    public Where eq(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.EQ),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //GT
    public Where gt(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.GT),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //LT
    public Where lt(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.LT),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //GE
    public Where ge(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.GT,WK.EQ),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //LE
    public Where le(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.LT,WK.EQ),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //between
    public Where between(Table table, String name, Object object,Object object1) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.BETWEEN),Arrays.asList(object,object1)), Arrays.asList(object,object1));
        return this;
    }

    //in
    public Where in(Table table, String name, List values) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.IN),values), values);
        return this;
    }

    //like
    public Where like(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.LIKE),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //null
    public Where isNull(Table table, String name) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.IS,WK.NULL),new ArrayList()), new ArrayList());
        return this;
    }

    //isNotNull
    public Where isNotNull(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.IS,WK.NOT,WK.NULL),new ArrayList()), new ArrayList());
        return this;
    }
    //notLike
    public Where notLike(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.NOT,WK.LIKE),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }

    //notIn
    public Where notIn(Table table, String name, List values) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.NOT,WK.IN),values), values);
        return this;
    }

    //notBetween
    public Where notBetween(Table table, String name, Object object,Object object1) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.NOT,WK.BETWEEN),Arrays.asList(object,object1)), Arrays.asList(object,object1));
        return this;
    }

    //neq
    public Where neq(Table table, String name, Object object) {
        caulse(table, name, WKUnit.getOperation(WK.op(WK.LT,WK.GT),Arrays.asList(object)), Arrays.asList(object));
        return this;
    }


    //解决条件需要的多个组合or and
    public Where or(Where where) {
        conditions.add(where);
        return this;
    }
}
