package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class InsertTest {

    @Test
    public void test1(){
        Table table = new Table("Test","A");
        SqlHelper sqlHelper = new SqlHelper(table);
        sqlHelper.ic(new Column(table,"testId",100));
        sqlHelper.ic(new Column(table,"testName","sam"));
        sqlHelper.ic(new Column(table,"testp",100));
        sqlHelper.ic(new Column(table,"XLSZ","111"));
        Expression expression = sqlHelper.insertBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());

    }
}
