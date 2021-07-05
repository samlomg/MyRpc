package com.dglbc.dbassistant;

import com.dglbc.dbassistant.base.Where;
import com.dglbc.dbassistant.check.T0027;
import com.dglbc.dbassistant.dml.Select;
import junit.framework.TestCase;

/**
 * @version 1.0
 * @Author LBC
 * @date 2021/6/30 21:35
 */
public class WhereTest extends TestCase {

    public void test1(){
        Where where = Where.me();
        where.eq("doco","2106300001");
        Select select = Select.create(T0027.class) ;
        select.where(where);
        TestUntils.isRight(select);
    }
}
