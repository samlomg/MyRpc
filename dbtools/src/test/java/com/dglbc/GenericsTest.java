package com.dglbc;

import com.dglbc.dbtools.Express;
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
        Express express =paramDao.select(where().eq(paramDao.obtainTable(),"name","LBC"));
        System.out.println(express.getSql()+"\n"+ express.getValues().toString());
        System.out.println("===============================");
        User user = new User(1,"lbc");
        Express expressioni =paramDao.insert(user);
        System.out.println(expressioni.getSql()+"\n"+expressioni.getValues().toString());
        System.out.println("===============================");
        Express expressionu =paramDao.update(user);
        System.out.println(expressionu.getSql()+"\n"+expressionu.getValues().toString());
    }
}
