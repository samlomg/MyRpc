package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.declear.CrudOperate;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.entity.User;
import junit.framework.TestCase;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class InsertTest extends TestCase {

    @Test
    public void test1(){
        Table table = new Table("Test","A");
        SQLHelper SQLHelper = new SQLHelper(table);
        SQLHelper.ic(new Column(table,"testId",100));
        SQLHelper.ic(new Column(table,"testName","sam"));
        SQLHelper.ic(new Column(table,"testp",100));
        SQLHelper.ic(new Column(table,"XLSZ","111"));
        Expression expression = SQLHelper.insertBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());

    }

    public void test2() throws IllegalAccessException {
        SQLHelper sqlHelper = new SQLHelper(User.class,new User(1,"lbc","EMK"),CrudOperate.INSERT);
        System.out.println(sqlHelper.insertBuilder().getSql());
    }
}
