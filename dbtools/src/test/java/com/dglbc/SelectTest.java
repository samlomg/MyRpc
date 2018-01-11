package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import org.junit.Test;

import static com.dglbc.dbtools.SQLBase.*;
import static com.dglbc.dbtools.SQLFuntion.*;

/**
 * Created by LBC on 2017/12/20
 **/

public class SelectTest{

    /*
        普通的查询语句
     */
    @Test
    public void test1() {
        Table table = table("Test","A");
        SQLHelper SQLHelper = new SQLHelper(table);
        Table f4211_t =table("F4211","B");
        Join f4211 = join(SQLKey.LEFTJOIN,f4211_t,column(table,"Sequence"),column(f4211_t,"Sequence"));
//        sqlHelper.sc(new Column(f4211_t,"SDDOCO"));
        SQLHelper.sc(as(isnull(column(f4211_t,"SDDOCO",0)),"SDDOCO"));
        SQLHelper.join(f4211);
        SQLHelper.where(where(SQLKey.AND).eq(column(table,"SHMCU","1110114")));
        SQLHelper.where(where(SQLKey.AND)
                .eq(convert("varchar(20)",expression(column(table,"SHDCTO"),false)),"SL"));
        SQLHelper.orderBy(column(table,"Sequence"));
        Expression expression = SQLHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }


    @Test
    public void test2() {
        //总结table 先定义

        Table table = table("F4201","A");
        Table f4211_t =table("F4211","B");
        SQLHelper SQLHelper = new SQLHelper(table);
        SQLHelper.sc(isnull(column(table,"SDDOCO","1111")));
        Join f4211 = join(SQLKey.LEFTJOIN,f4211_t,column(table,"Sequence"),column(f4211_t,"Sequence"));
        SQLHelper.join(f4211);
        SQLHelper.where(where().eq(column(table,"SHMCU","1110114")))
        .where(where().eq(column(f4211_t,"Sequence",236536)));
        SQLHelper.groupBy(column(table,"SDDOCO","1111")).having(where("")
                .gt(sum(expression(column(f4211_t,"SDDOCO"),false)),100000));//"SUM(B.SDDOCO) > 100000"
        Expression expression = SQLHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }

    //留着test3对比
    @Test
    public void test3() throws Exception {

        Table f0008t =new Table("F0008","A");

        SQLHelper SQLHelperT = new SQLHelper(f0008t);
        SQLHelperT.sc(f0008t,"drrt").sc(f0008t,"drky").sc(f0008t,"drdl01");
        SQLHelperT.where(new Where().eq(f0008t,"drsy", "42")).where(new Where().eq(f0008t,"drrt", "ZY"));


        Table table = new Table("F4201","A");
        Table f4211t =new Table("F4211","B");
        Table f0008_tt = new Table(SQLHelperT.selectBuilder(),"C");
        SQLHelper SQLHelper = new SQLHelper(table);
        Join f4211 = new Join(SQLKey.LEFTJOIN, f4211t, new Column(f4211t,"SDDOCO"), new Column(table,"SHDOCO")).on(new Column(f4211t,"SDDCTO"), new Column(table,"SDDCTO"));
        Join f0008 = new Join(SQLKey.LEFTJOIN, f0008_tt).on(new Column(f0008_tt,"drky"), new Column(f4211t,"SDZKYY"));
        SQLHelper.sc(f4211t, "SDDOCO");
        SQLHelper.sc(f0008_tt, "drdl01");
        SQLHelper.join(f4211);
        SQLHelper.join(f0008);
        SQLHelper.where(new Where().eq(table,"SHMCU", "1110114"))
                .where(new Where().eq(f4211t, "Sequence", 236536).and(new Where(SQLKey.OR).eq(table,"Sequence",236536)))
        ;
        SQLHelper.groupBy(new Column(f4211t, "SDDOCO")).having(new Where("").gt(sum(new Expression(new Column(f4211t,"SDDOCO"),false)),100000));
        Expression expression = SQLHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());

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
