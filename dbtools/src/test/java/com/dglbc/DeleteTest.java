package com.dglbc;

import com.dglbc.dbtools.ExecSql;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/21
 **/

public class DeleteTest {

    @Test
    public void test1() {
        SqlHelper sqlHelper = new SqlHelper("TEST");
        sqlHelper.where(new Where(SqlKey.AND).eq("testId", 1));
        ExecSql execSql = sqlHelper.deleteBulider();
        System.out.println(execSql.getSql());
        execSql.getValues().forEach(o -> System.out.println(o.toString()+" , "));
    }
}
