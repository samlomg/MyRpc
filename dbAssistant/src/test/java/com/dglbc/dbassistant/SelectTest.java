package com.dglbc.dbassistant;

import com.alibaba.druid.sql.SQLUtils;
import com.alibaba.druid.sql.ast.SQLStatement;
import com.alibaba.druid.sql.dialect.sqlserver.parser.SQLServerStatementParser;
import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.dml.Select;
import junit.framework.TestCase;

import java.util.List;

public class SelectTest extends TestCase {

    //简单的写一下sql
    public void  test1() throws Exception {
        Select select = new Select("id,name","Mytable"," a=2");
        select.column("age");
        String sql = select.build().sql().toString();
        SQLServerStatementParser parser = new SQLServerStatementParser(sql);
        boolean flag = false;
        try {
            List<SQLStatement> stmtList = parser.parseStatementList();
            System.out.println(SQLUtils.formatSQLServer(sql));
            System.out.println(select.values().toString());
            sql = select.pageSQLServerOld(10, 2, "sequence").sql().toString();
            parser = new SQLServerStatementParser(sql);
            stmtList = parser.parseStatementList();
            System.out.println(SQLUtils.formatSQLServer(sql));
            System.out.println(select.values().toString());
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
            System.err.println("包含语法查错误");
        }
        assertTrue(flag);

    }

    //简单
    public void test2() throws Exception {
        Select select = new Select("* from Mytable");
        select.where("a=?", 2).or("a=?", 1);
        System.out.println(select.build().sql());
        System.out.println(select.values().toString());
        System.out.println(select.pageSQLServerOld(10,2,"sequence desc").sql());
        System.out.println(select.values().toString());
    }

    //简单
    public void test3() throws Exception {
        Select select = new Select("A.name,A.tel from Mytable a");
        select.leftJoin("Address addr", " a.addr=addr.seq");
        select.where("name=?", 2).or("name=?", 1).eq("A.id",1);
        select.last(" order by a.id desc ");
        System.out.println(select.build().sql());
        System.out.println(select.values().toString());
        System.out.println(select.pageSQLServerOld(10,2,"sequence desc").sql());
        System.out.println(select.values().toString());
    }

    //between
    public void test4() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).between("age", 10, 20);
        select.last(" order by a.id desc ");
        System.out.println(select.build().sql());
    }

    // not between
    public void test5() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).notBetween("age", 10, 20);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }

    // not null
    public void test6() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).isNotNull("age");
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // in
    public void test7() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).in("age",10,20,30);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // not in
    public void test8() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).notIn("age",10,20,30);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // eq
    public void test9() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).eq("age",20);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // neq
    public void test10() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).neq("age",20);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // neq
    public void test101() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).neq("isnull(age,0)",20);
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }
    // notLike
    public void test11() throws Exception {
        Select select = new Select("*","Mytable");
        select.where("name=?", 2).notLike("age","%20%");
        select.last(" order by a.id desc ");
        Express express=select.build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }

    //test complicated case
    public void test12(){
        //left Join  F4103_92650901 a with(nolock) on s.KCLITM=A.PCLITM
        Select select = Select.create().column("s1.cxid as 促销活动编码")
                //接下来是试一下case when
//                .column()
                .column("s1.cxnm as 促销活动描述")
                .column("s1.cxstart","促销开始日期")
                .from("F41021_92650901","s")
                .leftJoin("F4103_92650901 ","A","s.KCLITM=A.PCLITM")
                .leftJoin("F4105_92650901 cost","A.pclitm=cost.pclitm and  A.pcmcu=cost.pcmcu")
                .leftJoin("( SELECT DISTINCT DHCODE,DHCCDSC  FROM F4001 WITH(NOLOCK)) ","E","A.PCSIZE=E.DHCODE")
                .leftJoin(Select.create().column("DHCODE").column("DHDESC").from("F4002","A").build(),"F"," A.PCGG=F.DHCODE");
        Express express=select.build();
        System.out.println(express.sql());
        //System.out.println(SQLUtils.formatSQLServer(express.sql().toString()));
        System.out.println(express.values().toString());

    }

}
