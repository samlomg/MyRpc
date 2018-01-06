package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public abstract class SQLFuntion {

    //datepart, number, expression
    public Expression dateAdd(String datepart, String number, Expression expression) {
        return new Expression(new StringBuilder().append(SqlKey.DATEADD).append(SqlKey.LEFT).append(datepart).
                append(",").append(number).append(",").append(expression.getSql()).append(SqlKey.RIGHT), expression.getValues());
    }

    public Expression dateAdd(String time, String datepart, String number) {
        return dateAdd(datepart, number, new Expression(time));
    }

    public Expression isnull(Expression expression, Object value) {
        List templist = new ArrayList();
        templist.addAll(expression.getValues());
        templist.add(value);
        return new Expression(new StringBuilder().append(SqlKey.ISNULL).append(SqlKey.LEFT).
                append(expression.getSql()).append(",?").append(SqlKey.RIGHT), templist);
    }

    public Expression isnull(Column column) {
        return isnull(new Expression(column, false), column.getValue());
    }

    public Expression as(Expression expression, String name) {
        return new Expression(expression.getSql().append(SqlKey.AS).append(name), expression.getValues());
    }

    public Expression dateDiff(String datepart,Expression expression,Expression expression1,String opt,int values) {
        List<Object> temp = new ArrayList<Object>();
        temp.addAll(expression.getValues());
        temp.addAll(expression1.getValues());
        temp.add(values);
        return new Expression(new StringBuilder().append(SqlKey.DATEDIFF).append(SqlKey.LEFT).append(datepart).append(",")
                .append(expression).append(",").append(expression1).append(SqlKey.RIGHT).append(opt).append(" ? "),temp);
    }

    public Expression convert(Expression expression, String dataType) {
        return new Expression(new StringBuilder().append(SqlKey.CONVERT).append(SqlKey.LEFT).append(expression.getSql())
                .append(",").append(dataType).append(SqlKey.RIGHT), expression.getValues());
    }

    public Expression caseWhen(Expression cas,Map<Expression,Expression> when,Expression els) {
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<Object>();
        //先处理case
        sql.append(SqlKey.CASE).append(cas.getSql()).append(" ");
        values.addAll(cas.getValues());

        //处理when
        for (Map.Entry<Expression,Expression> entry:when.entrySet()){
            sql.append(SqlKey.WHEN).append(entry.getKey().getSql()).append(SqlKey.THEN).append(entry.getValue().getSql()).append(" ");
            values.addAll(entry.getKey().getValues());
            values.addAll(entry.getValue().getValues());
        }
        //最后的else
        if (null !=els){
            sql.append(SqlKey.ELSE).append(els.getSql()).append(" ");
            values.addAll(els.getValues());
        }
        //end
        sql.append(SqlKey.END);

        return new Expression(sql,values);
    }

    public Expression sum(Expression expression){
        return new Expression(new StringBuilder().append(SqlKey.SUM).append(SqlKey.LEFT).append(expression.getSql()).append(SqlKey.RIGHT), expression.getValues());
    }

}
