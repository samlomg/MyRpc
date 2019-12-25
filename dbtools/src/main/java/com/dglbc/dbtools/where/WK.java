package com.dglbc.dbtools.where;

import com.dglbc.dbtools.Expression;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@AllArgsConstructor
public enum WK {
    IN("IN", " IN ( %s )"),
    BETWEEN("BETWEEN", " BETWEEN %s AND %s"),
    NULL("NULL", " NULL "),
    IS("IS", " IS %s "),
    EQ("=", " = %s "),
    LT("<", " < %s "),
    GT(">", " > %s "),
    NOT("NOT", " NOT %s "),
    LIKE("LIKE", " LIKE %s "),

    //SQL Functions
    AVG(" AVG (<expression>)", " AVG( %s ) "),
    COUNT(" COUNT (<expression>)", " COUNT( %s ) "),
    MAX(" MAX (<expression>)", " MAX( %s ) "),
    MIN(" MIN (<expression>)", " MIN( %s ) "),
    SUM(" SUM(<expression>)", " SUM( %s ) "),
    ROUND(" ROUND (expression, [decimal place])", " ROUND( %s , %s ) "),
    //string functions,
    CAST("CAST (expression AS [data type])", " CAST ( %s AS %s ) "),
    CONVERT("CONVERT (expression, [data type])", " CONVERT (%s, %s) "),
    SUBSTRING("SUBSTRING (str, position, [length])", " SUBSTRING (%s, %s, %s) "),
    LTRIM("LTRIM( [ [LOCATION] [remstr] FROM ] str)", "LTRIM( %s )"),
    RTRIM("RTRIM( [ [LOCATION] [remstr] FROM ] str)", "RTRIM( %s )"),
    LEN(" LEN (str)", " LEN( %s )"),
    REPLACE("REPLACE (str1, str2, str3)", " REPLACE (%s, %s, %s)"),
    //SQL Date Functions
    DATEADD("DATEADD (datepart, number, expression)", " DATEADD (%s, %s, %s) "),
    DATEDIFF("DATEDIFF (expression1, expression2)", " DATEDIFF (%s, %s) "),
    DATEPART("DATEPART (part_of_day, expression)", " DATEPART (%s, %s) "),
    GETDATE("GETDATE", " GETDATE() ");

    private String desc;
    private String format;

    //使用这个方法的前提是只有惟一一个%s 潘多拉盒子系列的函数
    public static String op(WK... wks) {
        String re = wks[0].getFormat();
        for (int i = 0; i < wks.length; i++) {
            if (i != 0) re = String.format(re, wks[i].getFormat());
        }
        return re;
    }


}
