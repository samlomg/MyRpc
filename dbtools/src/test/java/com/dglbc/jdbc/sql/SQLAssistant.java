package com.dglbc.jdbc.sql;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SQLHelper;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static com.dglbc.dbtools.SQLBase.where;

/**
 * Created by LBC on 2018/2/7
 **/

public abstract class SQLAssistant<T> {

    private Class<T> clazz;

    {
        Type type = getClass().getGenericSuperclass();
        if (type instanceof ParameterizedType) {
            ParameterizedType pType = (ParameterizedType) type;
            Type claz = pType.getActualTypeArguments()[0];
            if (claz instanceof Class) {
                this.clazz = (Class<T>) claz;
            }
        }
    }

    public Expression insert(T o) throws IllegalAccessException {
        //首先获取class的属性 来生成sql语句
        Field[] fields = clazz.getDeclaredFields();
        Table table = new Table(clazz.getSimpleName(), "A");
        SQLHelper sqlHelper = new SQLHelper(table);

        for (Field field : fields) {
            field.setAccessible(true);
            sqlHelper.ic(new Column(table, field.getName(), field.get(o)));
        }
        return sqlHelper.insertBuilder();
    }

    public Expression update(T o, Where... wheres) throws  IllegalAccessException {
        return update(o,null, wheres);
    }

    public Expression update(T o,String key, Where... wheres) throws IllegalAccessException {
        //首先获取class的属性 来生成sql语句
        Field[] fields = clazz.getDeclaredFields();
        Table table = new Table(clazz.getSimpleName(), "A");
        SQLHelper sqlHelper = new SQLHelper(table);
        key = key == null ? "sequence" : key;
        Field seq = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(key)){
                seq = field;
            }else {
                sqlHelper.uc(new Column(table, field.getName(), field.get(o)));
            }
        }
        if (wheres.length == 0) {
            sqlHelper.where(where().eq(table, "sequence", seq.get(o)));
        } else {
            for (Where where : wheres) {
                sqlHelper.where(where);
            }
        }
        return sqlHelper.updateBuilder();
    }

    public Expression select(Where... wheres) throws IllegalAccessException, NoSuchFieldException {
        //首先获取class的属性 来生成sql语句
        Field[] fields = clazz.getDeclaredFields();
        Table table = new Table(clazz.getSimpleName(), "A");
        SQLHelper sqlHelper = new SQLHelper(table);

        for (Field field : fields) {
            sqlHelper.sc(table, field.getName());
        }

        if (wheres.length != 0) {
            for (Where where : wheres) {
                sqlHelper.where(where);
            }
        }


        return sqlHelper.selectBuilder();
    }

    public Table obtainTable(){
        return  new Table(clazz.getSimpleName(), "A");
    }
}
