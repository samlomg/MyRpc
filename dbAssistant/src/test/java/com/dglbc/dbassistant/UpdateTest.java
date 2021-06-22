package com.dglbc.dbassistant;

import com.dglbc.dbassistant.dml.Update;
import junit.framework.TestCase;

public class UpdateTest extends TestCase {

    public void test1(){
        TestUntils.isRight(Update.TABLE("Sale").set("price", "100").set("member", "LBC").where("ID=?", 182));
    }
}
