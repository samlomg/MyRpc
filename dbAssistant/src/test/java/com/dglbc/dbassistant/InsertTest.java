package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.dml.Insert;
import junit.framework.TestCase;

public class InsertTest extends TestCase {

    public void  test(){
        Express express=Insert.into("Sale").value("orderId", 34).build();
        System.out.println(express.sql());
        System.out.println(express.values().toString());
    }

}
