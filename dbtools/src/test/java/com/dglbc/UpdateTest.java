package com.dglbc;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.declear.CrudOperate;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.entity.User;
import junit.framework.TestCase;

/**
 * Created by LBC on 2017/12/21
 **/

public class UpdateTest extends TestCase {

    public void test1(){
        Table table = new Table("TEST","A");
        SQLHelper SQLHelper = new SQLHelper(table);
        SQLHelper.uc(new Column(table, "testName", "SAMBC"));
//        SQLHelper.where(new Where(SQLKey.AND).eq(new Column(table,"testId",1)));
        Express express = SQLHelper.updateBuilder();
        System.out.println(express.getSql());
        System.out.println(express.getValues().toString());
    }

    public void test2() throws IllegalAccessException {
        SQLHelper sqlHelper = new SQLHelper(User.class,new User(1,"lbc","EMK"),CrudOperate.UPDATE);
        System.out.println(sqlHelper.updateBuilder().getSql());
    }
}
