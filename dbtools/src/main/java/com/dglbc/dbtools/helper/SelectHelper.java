package com.dglbc.dbtools.helper;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;

import java.util.ArrayList;
import java.util.List;

/**
 * select 列名 from 表名 A  xxx join xxx B on xxxx
 * where xxxx group by xxxx having xxxx order by xxxx
 */
public abstract class SelectHelper {

    //特殊的字 top distinct 等
    private Express specials;

    //100%的查询语句是带查询的列名表名
    //基础都用express会提供转化的方法
    private List<Express> selectContent = new ArrayList<>();

    private Table table;//主表
    private List<Where> wheres = new ArrayList<>(); //conditions

    //简化group by 和having的操作，直接自己写。这样的好处是不用
    //提供工具自行转换。
//    private List<>

}
