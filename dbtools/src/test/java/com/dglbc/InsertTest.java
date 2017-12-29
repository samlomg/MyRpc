package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlHelper;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class InsertTest {

    @Test
    public void test1(){
        SqlHelper sqlHelper = new SqlHelper("TEST");
        sqlHelper.ic("testId", 1);
        sqlHelper.ic("testName", "sam");
        sqlHelper.ic("testp", 101);
        sqlHelper.ic("XLSZ", "1111");
        Expression expression = sqlHelper.insertBuilder();
        System.out.println(expression.getSql());
        expression.getValues().forEach(o -> System.out.println(o.toString()+" , "));

    }
}
