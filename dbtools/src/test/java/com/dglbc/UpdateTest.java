package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class UpdateTest {

    @Test
    public void test1(){
        SqlHelper sqlHelper = new SqlHelper("TEST");
        sqlHelper.uc("testName", "SAMBC");
        sqlHelper.where(new Where(SqlKey.AND).eq("testId",1));
        Expression expression = sqlHelper.updateBuilder();
        System.out.println(expression.getSql());
        expression.getValues().forEach(o -> System.out.println(o.toString()+" , "));
    }
}
