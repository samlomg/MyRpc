package com.dglbc.dbassistant;

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

}
