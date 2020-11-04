package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.dml.Delete;
import junit.framework.TestCase;

public class DelteTest  extends TestCase {

    public void test1(){
        Express de = Delete.from("sale").where("ID=?", 44).build();
        System.out.println(de.sql());
        System.out.println(de.values().toString());
    }
}
