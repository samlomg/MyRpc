package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class DeleteTest {

    @Test
    public void test1() {
        Table table = new Table("Test","A");
        SqlHelper sqlHelper = new SqlHelper(table);
        sqlHelper.where(new Where(SqlKey.AND).eq(new Column(table,"testId",1)));
        Expression expression = sqlHelper.deleteBulider();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }
}
