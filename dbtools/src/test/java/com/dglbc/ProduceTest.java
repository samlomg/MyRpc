package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.produce.ParameterMode;
import org.junit.Test;

import java.sql.Types;

/**
 * Created by LBC on 2018/1/11
 **/

public class ProduceTest {
    @Test
    public  void test1(){
        SQLHelper sqlHelper = new SQLHelper();
        sqlHelper.call("call sp_test");
        sqlHelper.cc(ParameterMode.IN, 1, "lbc").cc(ParameterMode.IN,2,"备注")
                .cc(ParameterMode.OUT,3, Types.VARCHAR).cc(ParameterMode.INOUT,4,Types.INTEGER);

        Expression expression=sqlHelper.callBulider();
        System.out.println(expression.getSql());
    }
}
