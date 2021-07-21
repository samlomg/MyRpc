package com.dglbc.dbassistant;

import com.dglbc.dbassistant.check.T0027;
import com.dglbc.dbassistant.dml.Insert;
import junit.framework.TestCase;

public class InsertTest extends TestCase {

    public void  test(){
        TestUntils.isRight(Insert.into("Sale").value("orderId", 34));
    }

    public void test1(){
        T0027 t0027 = new T0027();
        t0027.setFlag(100);
        t0027.setGoodCode("608000010203");
        t0027.setMcu("1110112");
        t0027.setSequence(1);
        t0027.setZheKou("-2");
        t0027.setBegTime("2021-6-12");
        t0027.setEndTime("2021-7-12");

        Insert insert=Insert.into(t0027);
        TestUntils.isRight(insert);
    }

}
