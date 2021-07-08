package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Column;
import com.dglbc.dbassistant.dml.Update;
import junit.framework.TestCase;

/**
 * @version 1.0
 * @Author LBC
 * @date 2021/7/6 23:50
 */
public class ColumnTest extends TestCase {

    public void test1(){
        Column column = Column.create().set("name", "Lbc");
        Update update = Update.TABLE("T0027").set(column).where().eq("id",3);
        TestUntils.isRight(update);
    }
}
