package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.base.K;
import com.dglbc.dbassistant.dml.Update;
import com.dglbc.dbassistant.in.WK;
import junit.framework.TestCase;

public class UpdateTest extends TestCase {

    public void test1(){
        Express updatesql=Update.TABLE("Sale").set("price","100").set("member","LBC").where("ID=?",182).build();
        System.out.println(updatesql.sql());
        updatesql.values().forEach(o -> {
            System.out.println(o);
        });
    }
}
