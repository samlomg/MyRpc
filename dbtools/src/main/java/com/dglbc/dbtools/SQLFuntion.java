package com.dglbc.dbtools;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
SQLFuntion
 */
public class SQLFuntion {

    //datepart, number, expression
    public static Express dateAdd(String datepart, String number, Express express) {
        return new Express(new StringBuilder().append(SQLKey.DATEADD).append(SQLKey.LEFT).append(datepart).
                append(",").append(number).append(",").append(express.getSql()).append(SQLKey.RIGHT), express.getValues());
    }

    public static Express dateAdd(String time, String datepart, String number) {
        return dateAdd(datepart, number, new Express(time));
    }

    public static Express isnull(Express express, Object value) {
        List templist = new ArrayList();
        templist.addAll(express.getValues());
        templist.add(value);
        return new Express(new StringBuilder().append(SQLKey.ISNULL).append(SQLKey.LEFT).
                append(express.getSql()).append(",?").append(SQLKey.RIGHT), templist);
    }


    public static Express as(Express express, String name) {
        return new Express(express.getSql().append(SQLKey.AS).append(name), express.getValues());
    }

    public static Express dateDiff(String datepart, Express express, Express express1, String opt, int values) {
        List<Object> temp = new ArrayList<>();
        temp.addAll(express.getValues());
        temp.addAll(express1.getValues());
        temp.add(values);
        return new Express(new StringBuilder().append(SQLKey.DATEDIFF).append(SQLKey.LEFT).append(datepart).append(",")
                .append(express).append(",").append(express1).append(SQLKey.RIGHT).append(opt).append(" ? "), temp);
    }

    public static Express convert(String dataType, Express express) {
        return new Express(new StringBuilder().append(SQLKey.CONVERT).append(SQLKey.LEFT).append(dataType).append(",")
                .append(express.getSql()).append(SQLKey.RIGHT), express.getValues());
    }

    public static Express caseWhen(Express cas, Map<Express, Express> when, Express els) {
        StringBuilder sql = new StringBuilder();
        List<Object> values = new ArrayList<Object>();
        //先处理case
        sql.append(SQLKey.CASE).append(cas.getSql()).append(" ");
        values.addAll(cas.getValues());

        //处理when
        for (Map.Entry<Express, Express> entry : when.entrySet()) {
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

        return new Express(sql, values);
    }

    public static Express sum(Express express) {
        return new Express(new StringBuilder().append(SQLKey.SUM).append(SQLKey.LEFT).append(express.getSql()).append(SQLKey.RIGHT), express.getValues());
    }

    public static Express ltrim(Express express) {
        return new Express(new StringBuilder().append(SQLKey.LTRIM).append(SQLKey.LEFT).append(express.getSql()).append(SQLKey.RIGHT), express.getValues());
    }

    public static Express rtrim(Express express) {
        return new Express(new StringBuilder().append(SQLKey.RTRIM).append(SQLKey.LEFT).append(express.getSql()).append(SQLKey.RIGHT), express.getValues());
    }
}
