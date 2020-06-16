package com.dglbc.dbassistant.select;


import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.base.Join;
import com.dglbc.dbassistant.base.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * select 列名 from 表名 A  xxx join xxx B on xxxx
 * where xxxx
 * group by xxxx having xxxx order by xxxx
 *
 * important
 * 定位：这个是给自动生成的语句还能拓展成复杂语句的功能。
 * 如果是直接写的，其实可以不用这个。
 *
 */
public class SelectAssist {

    //特殊的字 top distinct 等
    private Express specials;

    //100%的查询语句是带查询的列名表名
    //基础都用express会提供转化的方法
    private List<Express> selectContent = new ArrayList<>();

    private Table table;//主表


    //一般设计的时候除了where 之外应该都是固定
    //这里我吧join也类似where的设计，等以后有需要的时候更方便处理
    private Join join;



    //20200130希望where条件能单独一个功能抽取出来，这样潘多拉盒子系列就有下一步了！
    //可以这么说用这个sql转换的人基本都对jdbc和db有一定的了解。
    // 所以我想where这个常用的动态的地方应该要做成可以灵活的。
    // 以便在任何地方的都有发挥余热的地方。
//    private List<Where> wheres = new ArrayList<>(); //conditions


    //简化group by 和having的操作，直接自己写。这样的好处是不用
    //提供工具自行转换。
//    private List<>

}
