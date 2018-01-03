package com.dglbc;

import com.dglbc.dbtools.SQLFuntion;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

/**
 * Created by LBC on 2017/12/20
 **/

public class SelectTest extends SQLFuntion{

    /*
        普通的查询语句
     */
    @Test
    public void test1() {
        Table table = new Table("Test","A");
        SqlHelper sqlHelper = new SqlHelper(table);
        Table f4211_t =new Table("F4211","B");
        Join f4211 = new Join(SqlKey.LEFTJOIN,f4211_t,new Column(table,"Sequence"),new Column(f4211_t,"Sequence"));
//        sqlHelper.sc(new Column(f4211_t,"SDDOCO"));
        sqlHelper.sc(as(isnull(new Column(f4211_t,"SDDOCO",0)),"SDDOCO"));
        sqlHelper.join(f4211);
        sqlHelper.where(new Where(SqlKey.AND).eq(new Column(table,"SHMCU","1110114")));
        sqlHelper.where(new Where(SqlKey.AND).eq(new Column(table,"SHDCTO","1110114")));
        sqlHelper.orderBy("A.Sequence");
        Expression expression = sqlHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }


    @Test
    public void test2() {
        Table table = new Table("F4201","A");
        Table f4211_t =new Table("F4211","B");
        SqlHelper sqlHelper = new SqlHelper(table);
        Join f4211 = new Join(SqlKey.LEFTJOIN,f4211_t,new Column(table,"Sequence"),new Column(f4211_t,"Sequence"));

        sqlHelper.sc(isnull(new Column(table,"SDDOCO","1111")));
        sqlHelper.join(f4211);
        sqlHelper.where(new Where().eq(new Column(table,"SHMCU","1110114")))
        .where(new Where().eq(new Column(f4211_t,"Sequence",236536)));
        sqlHelper.groupBy().having("SUM(B.SDDOCO) > 100000");
        Expression expression = sqlHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }

    @Test
    public void test3() throws Exception {
//        SqlHelper sqlHelperT = new SqlHelper("F0008");
//        sqlHelperT.sc("drrt").sc("drky").sc("drdl01");
//        sqlHelperT.where(new Where().eq("drsy", "42")).where(new Where().eq("drrt", "ZY"));
//
//
//        SqlHelper sqlHelper = new SqlHelper("F4201");
//        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "SDDOCO", "A", "SHDOCO").on("B","SDDCTO","SDDCTO");
//        Join f0008 = new Join(SqlKey.LEFTJOIN, sqlHelperT.selectBuilder(), "C", "drky", "B", "SDZKYY");
//        sqlHelper.sc(f4211, "SDDOCO");
//        sqlHelper.sc(f0008, "drdl01");
//        sqlHelper.join(f4211);
//        sqlHelper.join(f0008);
//        sqlHelper.where(new Where().eq("SHMCU", "1110114"))
//                .where(new Where().eq(f4211, "Sequence", 236536).and(new Where(SqlKey.OR).eq("Sequence",236536)))
//        ;
//        sqlHelper.groupBy().having("SUM(B.SDDOCO) > 100000");
//        Expression expression = sqlHelper.selectBuilder();
//        System.out.println(expression.getSql());
//        System.out.println(expression.getValues().toString());

//        List<String> a= JDBC.list(JDBC.getConnection(), statement.getSql(), new IVo<String>() {
//            @Override
//            public String row(ResultSet rs, int rowNum) throws Exception {
//                return rs.getString(2);
//            }
//        }, statement.getValues().toArray());
//        System.out.println(a.toString());
    }

    /*
查询语句where 带 运算
*/
    @Test
    public void test4() {
//        SqlHelper sqlHelper = new SqlHelper("F4201");
//        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");
//
//        sqlHelper.sc(f4211, "SDDOCO");
//        sqlHelper.join(f4211);
//        sqlHelper.where(new Where().eq("SHMCU", "1110114"));
//        Expression expression = sqlHelper.selectBuilder();
//        System.out.println(expression.getSql());
    }

}
