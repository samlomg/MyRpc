package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
SQLFuntion
 */
public class SQLFuntion {

    //datepart, number, expression
    public static Expression dateAdd(String datepart, String number, Expression expression) {
        return new Expression(new StringBuilder().append(SQLKey.DATEADD).append(SQLKey.LEFT).append(datepart).
                append(",").append(number).append(",").append(expression.getSql()).append(SQLKey.RIGHT), expression.getValues());
    }

    public static Expression dateAdd(String time, String datepart, String number) {
        return dateAdd(datepart, number, new Expression(time));
    }

    public static Expression isnull(Expression expression, Object value) {
        List templist = new ArrayList();
        templist.addAll(expression.getValues());
        templist.add(value);
        return new Expression(new StringBuilder().append(SQLKey.ISNULL).append(SQLKey.LEFT).
                append(expression.getSql()).append(",?").append(SQLKey.RIGHT), templist);
    }

    public static Expression isnull(Column column) {
        return isnull(new Expression(column, false), column.getValue());
    }

    public static Expression as(Expression expression, String name) {
        return new Expression(expression.getSql().append(SQLKey.AS).append(name), expression.getValues());
    }

    public static Expression dateDiff(String datepart, Expression expression, Expression expression1, String opt, int values) {
        List<Object> temp = new ArrayList<Object>();
        temp.addAll(expression.getValues());
        temp.addAll(expression1.getValues());
        temp.add(values);
        return new Expression(new StringBuilder().append(SQLKey.DATEDIFF).append(SQLKey.LEFT).append(datepart).append(",")
                .append(expression).append(",").append(expression1).append(SQLKey.RIGHT).append(opt).append(" ? "), temp);
    }

    public static Expression convert(String dataType, Expression expression) {
        return new Expression(new StringBuilder().append(SQLKey.CONVERT).append(SQLKey.LEFT).append(dataType).append(",")
                .append(expression.getSql()).append(SQLKey.RIGHT), expression.getValues());
    }

    public static Expression caseWhen(Expression cas, Map<Expression, Expression> when, Expression els) {
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<Object>();
        //先处理case
        sql.append(SQLKey.CASE).append(cas.getSql()).append(" ");
        values.addAll(cas.getValues());

        //处理when
        for (Map.Entry<Expression, Expression> entry : when.entrySet()) {
            sql.append(SQLKey.WHEN).append(entry.getKey().getSql()).append(SQLKey.THEN).append(entry.getValue().getSql()).append(" ");
            values.addAll(entry.getKey().getValues());
            values.addAll(entry.getValue().getValues());
        }
        //最后的else
        if (null != els) {
            sql.append(SQLKey.ELSE).append(els.getSql()).append(" ");
            values.addAll(els.getValues());
        }
        //end
        sql.append(SQLKey.END);

        return new Expression(sql, values);
    }

    public static Expression sum(Expression expression) {
        return new Expression(new StringBuilder().append(SQLKey.SUM).append(SQLKey.LEFT).append(expression.getSql()).append(SQLKey.RIGHT), expression.getValues());
    }

}
