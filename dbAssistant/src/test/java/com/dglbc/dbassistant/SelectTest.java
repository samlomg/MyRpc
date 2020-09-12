package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.dml.Select;
import junit.framework.TestCase;

public class SelectTest extends TestCase {

    //简单的写一下sql
    public void  test1() throws Exception {
        Select select = new Select("*","Mytable"," and a=2");
        System.out.println(select.build().sql());

    }

    //简单
    public void test2() throws Exception {
        Select select = new Select("* from Mytable");
        select.where("a=?", 2).or("a=?", 1);
        System.out.println(select.build().sql());
    }

    //简单
    public void test3() throws Exception {
        Select select = new Select("A.name,A.tel from Mytable a");
        select.leftJoin("Address addr", " a.addr=addr.seq");
        select.where("name=?", 2).or("name=?", 1).eq("A.id",1);
        select.last(" order by a.id desc ");
        System.out.println(select.build().sql());
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



}
