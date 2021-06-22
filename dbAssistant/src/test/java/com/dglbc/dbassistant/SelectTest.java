package com.dglbc.dbassistant;

import com.dglbc.dbassistant.check.T0027;
import com.dglbc.dbassistant.dml.Select;
import junit.framework.TestCase;

import static com.dglbc.dbassistant.TestUntils.isRight;

public class SelectTest extends TestCase {

    public void testClass() {
        Select select = new Select(T0027.class);
        String sql = select.build().sql().toString();
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(sql));

    }

    //简单的写一下sql
    public void test1() throws Exception {
        Select select = new Select("id,name", "Mytable", " a=2");
        select.column("age");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, "sequence"));
    }

    //简单
    public void test2() throws Exception {

        Select select = new Select("* from Mytable");
        select.where("a=?", 2).or("a=?", 1);
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    //简单
    public void test3() throws Exception {
        Select select = new Select("A.name,A.tel from Mytable a");
        select.leftJoin("Address addr", " a.addr=addr.seq");
        select.where("name=?", 2).or("name=?", 1).eq("A.id", 1);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    //between
    public void test4() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).between("age", 10, 20);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // not between
    public void test5() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).notBetween("age", 10, 20);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // not null
    public void test6() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).isNotNull("age");
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // in
    public void test7() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).in("age", 10, 20, 30);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // not in
    public void test8() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).notIn("age", 10, 20, 30);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // eq
    public void test9() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).eq("age", 20);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // neq
    public void test10() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).neq("age", 20);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // neq
    public void test101() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).neq("isnull(age,0)", 20);
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    // notLike
    public void test11() throws Exception {
        Select select = new Select("*", "Mytable");
        select.where("name=?", 2).notLike("age", "%20%");
        select.last(" order by a.id desc ");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));
    }

    //test complicated case
    public void test12() {
        //left Join  F4103_92650901 a with(nolock) on s.KCLITM=A.PCLITM
        Select select = Select.create().column("s1.cxid as 促销活动编码")
                //接下来是试一下case when
                .column(" (case when isnull(KCLIRG,0)>0 then 2 when isnull(KCLORG,0)>0 then 3 else 1  end) as rowsType ")
                .column("s1.cxnm as 促销活动描述")
                .column("s1.cxstart", "促销开始日期")
                .from("F41021_92650901", "s")
                .leftJoin("F4103_92650901 ", "A", "s.KCLITM=A.PCLITM")
                .leftJoin("F4105_92650901 cost", "A.pclitm=cost.pclitm and  A.pcmcu=cost.pcmcu")
                .leftJoin("( SELECT DISTINCT DHCODE,DHCCDSC  FROM F4001 WITH(NOLOCK)) ", "E", "A.PCSIZE=E.DHCODE")
                .leftJoin(Select.create().column("DHCODE").column("DHDESC").from("F4002", "A").build(), "F", " A.PCGG=F.DHCODE");
        System.out.println("============正常检查普通表查询=============");
        assertTrue(isRight(select, false));
        String key = "sequence desc";
        System.out.println("============正常检查普通表分页查询=============");
        assertTrue(isRight(select, true, key));

    }

}
