package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class UpdateTest {

    @Test
    public void test1(){
        Table table = new Table("TEST","A");
        SQLHelper SQLHelper = new SQLHelper(table);
        SQLHelper.uc(new Column(table, "testName", "SAMBC"));
        SQLHelper.where(new Where(SQLKey.AND).eq(new Column(table,"testId",1)));
        Expression expression = SQLHelper.updateBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }
}
