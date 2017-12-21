package com.dglbc;

import com.dglbc.dbtools.ExecSql;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/20
 **/

public class SelectTest {

    /*
        普通的查询语句
     */
    @Test
    public void test1() {
        SqlHelper sqlHelper = new SqlHelper("F4201");
        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");

        sqlHelper.sc(f4211, "SDDOCO");
        sqlHelper.addJoin(f4211);
        sqlHelper.addWhere(new Where(SqlKey.AND).eq("SHMCU", "1110114"));
        sqlHelper.orderBy("A.Sequence");
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
    }

    @Test
    public void test2() {
        SqlHelper sqlHelper = new SqlHelper("F4201");
        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");

        sqlHelper.sc(f4211, "SDDOCO");
        sqlHelper.addJoin(f4211);
        sqlHelper.addWhere(new Where(SqlKey.AND).eq("SHMCU", "1110114"))
        .addWhere(new Where(SqlKey.AND).eq(f4211, "Sequence", 236536));
        sqlHelper.groupBy().having("SUM(B.SDDOCO) > 100000");
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
    }

}
