package com.dglbc.dbassistant;

import com.dglbc.dbassistant.dml.Insert;
import junit.framework.TestCase;

public class InsertTest extends TestCase {

    public void  test(){
        TestUntils.isRight(Insert.into("Sale").value("orderId", 34));
    }

}
