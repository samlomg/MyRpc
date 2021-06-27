package com.dglbc.dbassistant;

import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.dglbc.dbassistant.base.DML;
import com.dglbc.dbassistant.dml.Delete;
import com.dglbc.dbassistant.dml.Insert;
import com.dglbc.dbassistant.dml.Select;
import com.dglbc.dbassistant.dml.Update;

import java.util.List;

public class TestUntils {
    public static boolean isRight(String sql) {
        boolean flag = true;
        System.out.println(sql);
        try {
            SQLServerStatementParser parser = new SQLServerStatementParser(sql);
            parser = new SQLServerStatementParser(sql);
            List<SQLStatement> stmtList = parser.parseStatementList();
            System.out.println("===========OK===============");
        } catch (Exception e) {
            System.err.println("包含语法查错误");
            flag = false;
        }
        return flag;
    }

    public static boolean isRight(Select select, boolean isPage, String... key) {
        return isRight(isPage ? select.pageSQLServerOld(10, 2, key[0]).sql().toString() : select.build().sql().toString());
    }

    public static  boolean isRight(DML dml){
        return isRight(dml.build().sql().toString());
    }
}
