package com.dglbc.dbassistant;

import com.dglbc.dbassistant.check.T0027;
import com.dglbc.dbassistant.dml.Update;
import junit.framework.TestCase;

public class UpdateTest extends TestCase {

    public void test1(){
        TestUntils.isRight(Update.TABLE("Sale").set("price", "100").set("member", "LBC").where("ID=?", 182));
    }

    public void test2(){
        T0027 t0027 = new T0027();
        t0027.setGoodCode("6080188801");
        t0027.setFlag(1);
        t0027.setZheKou("-1");
        t0027.setBegTime("2021-7-21");
        t0027.setEndTime("2021-7-31");
        TestUntils.isRight(Update.TABLE(t0027));
    }
}
