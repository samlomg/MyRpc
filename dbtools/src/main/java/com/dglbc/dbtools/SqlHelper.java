package com.dglbc.dbtools;

import com.dglbc.dbtools.join.Join;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
public class SqlHelper implements Serializable {

    public static String AND = " AND ";
    public static String OR = " OR ";
    public static String WHERE = " WHERE ";
    public static String COM = " 1=1 ";
    public static String ORDER = " ORDER BY ";
    public static String GROUP = " GROUP BY ";
    public static String ON = " ON ";
    public static String LEFTJOIN = " LEFTJOIN ";
    public static String RIGHTJOIN = " RIGHTJOIN ";
    public static String INNERJOIN = " INNERJOIN ";


    private List<String> selectContent;
    private List<Unit> insertContent;
    private List<Unit> updateContent;
    private List<Condition> condition;
    private String table;
    private List<Join> join;

    private List<Object> params;

    private String order;

    public SqlHelper(String table) {
        this.table = table;
        this.selectContent = new ArrayList<>();
        this.insertContent = new ArrayList<>();
        this.updateContent = new ArrayList<>();
    }

    //查询语句
    public SqlHelper sc(String name) {
        this.selectContent.add("A."+name+" AS "+name.toUpperCase());
        return this;
    }

    //查询语句
    public SqlHelper sc(Join alias,String name) {
        this.selectContent.add(alias.getAlias()+"."+name +" AS "+name.toUpperCase());
        return this;
    }

    //查询语句
    public SqlHelper sc(Join alias,String name,String aname) {
        this.selectContent.add(alias.getAlias()+"."+name +" AS "+aname);
        return this;
    }

    //插入语句
    public SqlHelper ic(String name, Object value) {
        this.insertContent.add(new Unit(name, value));
        return this;
    }

    //更新语句
    public SqlHelper uc(String name, Object value) {
        this.updateContent.add(new Unit(name, value));
        return this;
    }

}
