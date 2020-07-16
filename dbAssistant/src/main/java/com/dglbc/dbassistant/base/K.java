package com.dglbc.dbassistant.base;

/**
 * Created by LBC on 2017/12/19
 **/

public interface K {
    String SELECT = "SELECT ";
    String INSERT = "INSERT INTO ";
    String UPDATE = "UPDATE ";
    String DELETE = "DELETE ";
    String WITH = " WITH(NOLOCK) ";
    String AND = " AND ";
    String OR = " OR ";
    String WHERE = " \nWHERE 1=1 ";
    String ORDER = " \nORDER BY ";
    String GROUP = " \nGROUP BY ";
    String ON = " ON ";
    String LEFTJOIN = " \nLEFT JOIN ";
    String RIGHTJOIN = " \nRIGHT JOIN ";
    String INNERJOIN = " \nINNER JOIN ";
    String BETWEEN = " BETWEEN ";
    String IN = " IN ";
    String FROM = " \nFROM ";
    String HAVING = " \nHAVING ";
    String VALUES = " VALUES ";
    String SET = " SET ";
    String AS = " AS ";

    String LEFT = " ( ";
    String RIGHT = " ) ";
    String ISNULL = " ISNULL ";

    String TOP = " TOP ";
    String DISTINCT  = " DISTINCT ";

    String SUM = " SUM ";
    String LTRIM = " LTRIM ";
    String RTRIM = " RTRIM ";
    String DATEADD = " DATEADD ";
    String DATEDIFF = " DATEDIFF ";
    String CONVERT = " CONVERT ";

    String CASE = " CASE ";
    String WHEN = " WHEN ";
    String THEN = " THEN ";
    String ELSE = " ELSE ";
    String END = " END ";
}
