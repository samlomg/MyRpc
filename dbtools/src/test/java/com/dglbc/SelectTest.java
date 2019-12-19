package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.SQLKey;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import com.dglbc.entity.User;
import com.dglbc.jdbc.JDBC;
import com.dglbc.jdbc.face.IVo;
import org.junit.Test;

import java.sql.ResultSet;
import java.util.List;

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
        Table table = table("Test", "A");
        SQLHelper sqlHelper = new SQLHelper(table);
        Table f4211_t = table("F4211", "B");
        Join f4211 = join(SQLKey.LEFTJOIN, f4211_t, column(table, "Sequence"), column(f4211_t, "Sequence"));
//        sqlHelper.sc(new Column(f4211_t,"SDDOCO"));
        sqlHelper.as(SQLKey.TOP + " 10")
                .sc(as(isnull(column(f4211_t, "SDDOCO", 0)), "SDDOCO"))
                .join(f4211)
//                .where(where(SQLKey.AND).eq(column(table, "SHMCU", "1110114")))
//                .where(where(SQLKey.AND).eq(convert("varchar(20)", expression(column(table, "SHDCTO"), false)), "SL"))
                .orderBy(column(table, "Sequence"));
        Expression expression = sqlHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }


    @Test
    public void test2() {
        //总结table 先定义

        Table table = table("F4201", "A");
        Table f4211_t = table("F4211", "B");
        SQLHelper SQLHelper = new SQLHelper(table);
        SQLHelper.sc(isnull(column(table, "SDDOCO", "1111")));
        Join f4211 = join(SQLKey.LEFTJOIN, f4211_t, column(table, "Sequence"), column(f4211_t, "Sequence"));
        SQLHelper.join(f4211);
//        SQLHelper.where(where().eq(column(table, "SHMCU", "1110114")))
//                .where(where().eq(column(f4211_t, "Sequence", 236536)));
//        SQLHelper.groupBy(column(table, "SDDOCO", "1111")).having(where("")
//                .gt(sum(expression(column(f4211_t, "SDDOCO"), false)), 100000));//"SUM(B.SDDOCO) > 100000"
        Expression expression = SQLHelper.selectBuilder();
        System.out.println(expression.getSql());
        System.out.println(expression.getValues().toString());
    }

    //留着test3对比
    @Test
    public void test3() throws Exception {

        Table f0008t = new Table("F0008", "A");

        SQLHelper SQLHelperT = new SQLHelper(f0008t);
        SQLHelperT.sc(f0008t, "drrt").sc(f0008t, "drky").sc(f0008t, "drdl01");
        SQLHelperT.where(new Where().eq(f0008t, "drsy", "42")).where(new Where().eq(f0008t, "drrt", "ZY"));


        Table table = new Table("F4201", "A");
        Table f4211t = new Table("F4211", "B");
        Table f0008_tt = new Table(SQLHelperT.selectBuilder(), "C");
        SQLHelper SQLHelper = new SQLHelper(table);
        Join f4211 = new Join(SQLKey.LEFTJOIN, f4211t, new Column(f4211t, "SDDOCO"), new Column(table, "SHDOCO")).on(new Column(f4211t, "SDDCTO"), new Column(table, "SDDCTO"));
        Join f0008 = new Join(SQLKey.LEFTJOIN, f0008_tt).on(new Column(f0008_tt, "drky"), new Column(f4211t, "SDZKYY"));
        SQLHelper.sc(f4211t, "SDDOCO");
        SQLHelper.sc(f0008_tt, "drdl01");
        SQLHelper.join(f4211);
        SQLHelper.join(f0008);
        SQLHelper.where(new Where().eq(table, "SHMCU", "1110114"))
//                .where(new Where().eq(f4211t, "Sequence", 236536).and(new Where(SQLKey.OR).eq(table, "Sequence", 236536)))
        ;
//        SQLHelper.groupBy(new Column(f4211t, "SDDOCO")).having(new Where("").gt(sum(new Expression(new Column(f4211t, "SDDOCO"), false)), 100000));
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
    public void test4() throws Exception {
        Table f4201 = new Table("F4201", "A");
        Table f4211 = new Table("F4211", "B");
        SQLHelper sqlHelper = new SQLHelper(f4201);
        sqlHelper.sc(f4201, "SHDOCO").sc(column(f4201, "SHMCU")).sc(f4201, "Sequence")
                .join(join(SQLKey.LEFTJOIN, f4211).on(column(f4201, "Sequence"), column(f4211, "Sequence")))
//                .eq("SDMCU", "1110114", f4211)
                .as(SQLKey.TOP + "10");
        System.out.println(sqlHelper.selectBuilder().getSql());
        List<Integer> a = JDBC.list(sqlHelper.selectBuilder(), new IVo<Integer>() {
            @Override
            public Integer row(ResultSet rs, int rowNum) throws Exception {
                return rs.getInt("Sequence");
            }
        });
        a.forEach(System.out::println);
    }


    @Test
    public void test5() {
        SQLHelper sqlHelper = new SQLHelper(User.class);
        System.out.println(sqlHelper.selectBuilder().getSql());
    }

}
