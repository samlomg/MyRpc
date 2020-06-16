package com.dglbc.dbtools.where;

import com.dglbc.dbtools.exception.TipsShow;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@AllArgsConstructor
public enum WK {
    IN("IN", " IN ( %s )",1),
    BETWEEN("BETWEEN", " BETWEEN %s AND %s",2),
    NULL("NULL", " NULL ",0),
    IS("IS", " IS %s ",1),
    EQ("=", " = %s ",1),
    LT("<", " < %s ",1),
    GT(">", " > %s ",1),
    NOT("NOT", " NOT %s ",1),
    LIKE("LIKE", " LIKE %s ",1),

    ISNULL("ISNULL(<expression>)","ISNULL( %s )",1),

    //SQL Functions
    AVG(" AVG (<expression>)", " AVG( %s ) ",1),
    COUNT(" COUNT (<expression>)", " COUNT( %s ) ",1),
    MAX(" MAX (<expression>)", " MAX( %s ) ",1),
    MIN(" MIN (<expression>)", " MIN( %s ) ",1),
    SUM(" SUM(<expression>)", " SUM( %s ) ",1),
    ROUND(" ROUND (expression, [decimal place])", " ROUND( %s , %s ) ",2),
    //string functions,
    CAST("CAST (expression AS [data type])", " CAST ( %s AS %s ) ",2),
    CONVERT("CONVERT (expression, [data type])", " CONVERT (%s, %s) ",2),
    SUBSTRING("SUBSTRING (str, position, [length])", " SUBSTRING (%s, %s, %s) ",3),
    LTRIM("LTRIM( [ [LOCATION] [remstr] FROM ] str)", "LTRIM( %s )",1),
    RTRIM("RTRIM( [ [LOCATION] [remstr] FROM ] str)", "RTRIM( %s )",1),
    LEN(" LEN (str)", " LEN( %s )",1),
    REPLACE("REPLACE (str1, str2, str3)", " REPLACE (%s, %s, %s)",3),
    //SQL Date Functions
    DATEADD("DATEADD (datepart, number, expression)", " DATEADD (%s, %s, %s) ",3),
    DATEDIFF("DATEDIFF (expression1, expression2)", " DATEDIFF (%s, %s) ",2),
    DATEPART("DATEPART (part_of_day, expression)", " DATEPART (%s, %s) ",2),
    GETDATE("GETDATE", " GETDATE() ",0);

    private String desc;
    private String format;
    private int va;

    //使用这个方法的前提是只有惟一一个%s 潘多拉盒子系列的函数(祸福难析)
    //叠function
    public static String op(WK... wks) {
        if (wks[0].getVa() !=1) TipsShow.alert("参数大于1");
        String re = wks[0].getFormat();
        for (int i = 1; i < wks.length; i++) {
            if (i != wks.length && wks[i].getVa() !=1) TipsShow.alert("参数大于1");
            re = String.format(re, wks[i].getFormat());
        }
        return re;
    }


}
