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

    public Expression dateDiff() {
        return new Expression();
    }

    public Expression convert(Expression expression, String dataType) {
        return new Expression(new StringBuilder().append(SqlKey.CONVERT).append(SqlKey.LEFT).append(expression.getSql())
                .append(",").append(dataType).append(SqlKey.RIGHT), expression.getValues());
    }

    public Expression caseWhen(Expression cas,Map<Expression,Expression> when,Expression... els) {
        return new Expression();
    }

}
