package com.dglbc.dbassistant;

import com.dglbc.dbassistant.dml.Delete;
import junit.framework.TestCase;

public class DelteTest  extends TestCase {

    public void test1(){
        TestUntils.isRight(Delete.from("sale").where("ID=?", 44));
    }
}
