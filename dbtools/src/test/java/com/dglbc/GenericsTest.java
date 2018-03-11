package com.dglbc;

import com.dglbc.dbtools.Expression;
import com.dglbc.entity.User;
import com.dglbc.jdbc.sql.ParamDao;
import org.junit.Test;

import static com.dglbc.dbtools.SQLBase.where;

/**
 * Created by LBC on 2018/2/7
 **/

public class GenericsTest {

    @Test
    public void test(){
        ParamDao paramDao = new ParamDao();
//        paramDao.update();
    }

    @Test
    public void  test2() throws NoSuchFieldException, IllegalAccessException {
        System.out.println(ParamDao.class.getSimpleName());
        ParamDao paramDao=new ParamDao();
        Expression expression =paramDao.select(where().eq(paramDao.obtainTable(),"name","LBC"));
        System.out.println(expression.getSql()+"\n"+expression.getValues().toString());
        System.out.println("===============================");
        User user = new User(1,"lbc");
        Expression expressioni =paramDao.insert(user);
        System.out.println(expressioni.getSql()+"\n"+expressioni.getValues().toString());
        System.out.println("===============================");
        Expression expressionu =paramDao.update(user);
        System.out.println(expressionu.getSql()+"\n"+expressionu.getValues().toString());
    }
}
