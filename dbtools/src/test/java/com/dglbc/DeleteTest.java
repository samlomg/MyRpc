package com.dglbc;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.table.Table;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class DeleteTest {

    @Test
    public void test1() {
        Table table = new Table("Test","A");
        SQLHelper SQLHelper = new SQLHelper(table);
//        SQLHelper.where(new Where(SQLKey.AND).eq(new Column(table,"testId",1)));
        Express express = SQLHelper.deleteBulider();
        System.out.println(express.getSql());
        System.out.println(express.getValues().toString());
    }
}
