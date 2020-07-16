package com.dglbc.dbassistant.dml;


import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;
import com.sun.xml.internal.ws.client.sei.ValueSetter;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * select 列名 from 表名 A  xxx join xxx B on xxxx
 * where xxxx
 * group by xxxx having xxxx order by xxxx
 * <p>
 * important
 * 定位：这个是给自动生成的语句还能拓展成复杂语句的功能。
 * 如果是直接写的，其实可以不用这个。
 * 还有一点是，如果注重安全性，这里也提供参数和语句分离的方法。
 * （可以直接在express里面填充参数也可以用？然后传参到value）
 */
@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Select extends Express {

    //特殊的字 top distinct 等
    private FirstExpress first;

    /**
     * 100%的查询语句是带查询的列名表名
     * 基础都用express会提供转化的方法
     */
    private Column columns;

    private Table table;//主表有查询语句必有主表查询的

    /**
     * 一般设计的时候除了where 之外应该都是固定
     * 这里我吧join也类似where的设计，等以后有需要的时候更方便处理
     */
    private Join joins;

    /**
     * 20200130希望where条件能单独一个功能抽取出来，这样潘多拉盒子系列就有下一步了！
     * 可以这么说用这个sql转换的人基本都对jdbc和db有一定的了解。
     * 所以我想where这个常用的动态的地方应该要做成可以灵活的。
     * 以便在任何地方的都有发挥余热的地方。
     */
    private Where wheres;

    /**
     * 简化group by 和having的操作，直接自己写。这样的好处是不用
     * 提供工具自行转换。
     */
    private OtherExpress others;

    public Express build() {
        //初始化

        //build

        this.sql().append(K.SELECT);
        checkParts(first);
        checkParts(columns);
        checkParts(table);
        checkParts(joins);
        checkParts(wheres);
        checkParts(others);


        //重要一点如果是已经构建生成的express必须有标志
        this.sec(true);
        return this;
    }

    private void checkParts(AbstractExpress abstractExpress) {
        if (abstractExpress == null) {

        } else {
            Response response = abstractExpress.isCheck();
            if (response.code() == 200 || response.code() == 10001) {
                try {
                    this.merge(abstractExpress.toExpress());
                } catch (Exception e) {
                    e.printStackTrace();
                    TipsShow.alert("merge fail");
                }
            }
        }

    }


    //先来最多的情况
    public Select(String col, String tab, String where) {

        this.columns = new Column(new Express(col));
        this.table = new Table(tab);
        //todo 目前没想到什么好的办法只能先new然后赋值
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres = new Where(specialExpress);
    }

    public Select(String sqlAll) {
        this.first = new FirstExpress(sqlAll);
    }

    /**
     * 分割线下面是为了方便增加的工具方法不是core
     * ==================================
     */

    public Select sc(String columns,Object... values) {
        if (this.columns() == null ) this.columns = new Column();
        this.columns.columns().add(values.length == 0 ? new Express(columns): new Express(columns,Arrays.asList(values)));
        return this;
    }

    public Select where(String where, Object... values) {
        if (null == this.wheres()) {
            this.wheres = new Where();
        }
        this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where) : new SpecialExpress(where, Arrays.asList(values)));
        return this;
    }

    public Select where(String cateNate, String where, Object... values) {
        if (cateNate == null || cateNate.trim() == "") {
            where(where, values);
        } else {

            if (null == this.wheres()) {
                this.wheres = new Where();
            }
            this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where, cateNate) : new SpecialExpress(where, Arrays.asList(values), cateNate));
        }
        return this;
    }

    public Select and(String where, Object... values) {
        where(where, values);
        return this;
    }

    public Select or(String where, Object... values) {
        where(K.OR, where, values);
        return this;
    }


    public Select join(String tab,String ons){
        //todo 20200716待完善
        return this;
    }

}
