package com.dglbc;

import com.dglbc.dbtools.ExecSql;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.jdbc.JDBC;
import com.dglbc.dbtools.jdbc.face.IVo;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.List;

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
        sqlHelper.join(f4211);
        sqlHelper.where(new Where(SqlKey.AND).eq("SHMCU", "1110114"));
        sqlHelper.orderBy("A.Sequence");
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
    }

    @Test
    public void test2() {
        SqlHelper sqlHelper = new SqlHelper("F4201");
        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");

        sqlHelper.sc(f4211, "SDDOCO");
        sqlHelper.join(f4211);
        sqlHelper.where(new Where(SqlKey.AND).eq("SHMCU", "1110114"))
        .where(new Where(SqlKey.AND).eq(f4211, "Sequence", 236536));
        sqlHelper.groupBy().having("SUM(B.SDDOCO) > 100000");
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
    }

    @Test
    public void test3() throws Exception {
        SqlHelper sqlHelperT = new SqlHelper("F0008");
        sqlHelperT.sc("drrt").sc("drky").sc("drdl01");
        sqlHelperT.where(new Where().eq("drsy", "42")).where(new Where().eq("drrt", "ZY"));


        SqlHelper sqlHelper = new SqlHelper("F4201");
        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");
        Join f0008 = new Join(SqlKey.LEFTJOIN, sqlHelperT.selectBuilder(), "C", "drky", "B", "SDZKYY");
        sqlHelper.sc(f4211, "SDDOCO");
        sqlHelper.sc(f0008, "drdl01");
        sqlHelper.join(f4211);
        sqlHelper.join(f0008);
        sqlHelper.where(new Where(SqlKey.AND).eq("SHMCU", "1110114"))
                //.where(new Where(SqlKey.AND).eq(f4211, "Sequence", 236536))
        ;
        sqlHelper.groupBy().having("SUM(B.SDDOCO) > 100000");
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
        System.out.println(execSql.getValues().toString());

        List<String> a= JDBC.list(JDBC.getConnection(), execSql.getSql(), new IVo<String>() {
            @Override
            public String row(ResultSet rs, int rowNum) throws Exception {
                return rs.getString(2);
            }
        },execSql.getValues().toArray());
        System.out.println(a.toString());
    }

}
