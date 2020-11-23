package com.dglbc.dbassistant.dml;


import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
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


    //todo 把直接赋值sql的函数清除

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
        if (sec()) clear();


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

    /**
     * sqlServer 2005 以后能用的分页
     * @param size
     * @param page
     * @param key
     * @return
     */
    public Express pageSQLServerOld(int size, int page, String key) {
        if (sec()) clear();
        sql().append("SELECT Top ? *  FROM ( SELECT ROW_NUMBER() OVER(Order by ").append(key).append(") AS RowId,");
        values().add(size);
        checkParts(columns);
        checkParts(table);
        checkParts(joins);
        checkParts(wheres);
        checkParts(others);
        sql().append(") A  WHERE RowId between ? and ? ");
        values().add((page - 1) * size + 1);
        values().add(page * size);
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

    //补充first
    public Select af(String sql) {
        this.first.af(sql);
        return this;
    }

    public Select af(Express sql) {
        this.first.af(sql);
        return this;
    }


    //先来最多的情况
    public Select(String col, String tab, String where) {

        this.columns = new Column(new Express(col));
        this.table = new Table(tab,true);
        //todo 目前没想到什么好的办法只能先new然后赋值
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres = new Where(specialExpress);
    }

    //补充情况
    public Select(String col, String tab) {
        this.columns = new Column(new Express(col));
        this.table = new Table(tab,true);
    }

    public Select(String sqlAll) {
        this.first = new FirstExpress(sqlAll);
    }


    /**
     * 分割线下面是为了方便增加的工具方法不是core
     * ==================================
     */

    public static Select create(){
        return new Select();
    }

    public Select column(String columns, Object... values) {
        if (this.columns() == null) this.columns = new Column();
        this.columns.columns().add(values.length == 0 ? new Express(columns) : new Express(columns, Arrays.asList(values)));
        return this;
    }

    public Select column(Express column) {
        if (columns == null) {
            //todo 建立日志系统
        } else {
            if (this.columns() == null) this.columns = new Column();
            this.columns.columns().add(column);
        }
        return this;
    }

    public Select from(String table,String alias){
        if (table() == null){
            this.table = new Table(table,alias,true);
        }else {
            this.table = this.table.tableName(table).alias(alias);
        }
        return this;
    }

    public Select where(){
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


    public Select join(String tab, String on) {
        //todo 20200716待完善
        if (joins == null) {
            joins = new Join();
        }
        this.joins.joins().add(new ExpressWithTable(tab, on));

        return this;
    }

    public Select leftJoin(String tab, String on) {
        join(tab, on);
        return this;
    }

    public Select innerJoin(String tab, String on) {
        if (joins == null) {
            joins = new Join();
        }
        this.joins.joins().add(new ExpressWithTable(K.INNERJOIN, tab, on));
        return this;
    }

    public Select last(String by) {
        if (this.others == null) {
            this.others = new OtherExpress();
        }
        others.others().add(new Express(by));
        return this;
    }

    /**
     * where case
     */

    /**
     * commond
     */

    public Select where(String cateNate, String column, WK wk, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Select where(String cateNate, String column, String wks, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(wks, value), value);
        return this;
    }


    public Select where(String cateNate, Express express, WK wk, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Select where(String cateNate, Express express, String wks, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(wks, value), value);
        return this;
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public Select eq(String column, Object value) {
        where(K.AND, column, WK.EQ, value);
        return this;
    }

    /**
     * 等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select eq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.EQ, value);
        return this;
    }

    public Select eq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.EQ, value);
        return this;
    }

    /**
     * >
     *
     * @param column
     * @param value
     * @return
     */
    public Select gt(String column, Object value) {
        where(K.AND, column, WK.GT, value);
        return this;
    }

    /**
     * >
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select gt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.GT, value);
        return this;
    }

    public Select gt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.GT, value);
        return this;
    }


    /**
     * <
     *
     * @param column
     * @param value
     * @return
     */
    public Select lt(String column, Object value) {
        where(K.AND, column, WK.LT, value);
        return this;
    }

    /**
     * <
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select lt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LT, value);
        return this;
    }

    public Select lt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LT, value);
        return this;
    }


    /**
     * >=
     *
     * @param column
     * @param value
     * @return
     */
    public Select ge(String column, Object value) {
        where(K.AND, column, WK.op(WK.GT, WK.EQ), value);
        return this;
    }

    /**
     * >=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select ge(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.GT, WK.EQ), value);
        return this;
    }

    public Select ge(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.GT, WK.EQ), value);
        return this;
    }


    /**
     * <=
     *
     * @param column
     * @param value
     * @return
     */
    public Select le(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.EQ), value);
        return this;
    }

    /**
     * <=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select le(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.EQ), value);
        return this;
    }

    public Select le(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.EQ), value);
        return this;
    }


    /**
     * between
     *
     * @param column
     * @param value
     * @return
     */
    public Select between(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    /**
     * between
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select between(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    public Select between(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    /**
     * in
     *
     * @param column
     * @param value
     * @return
     */
    public Select in(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }

    /**
     * in
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select in(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }

    public Select in(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }


    /**
     * like
     *
     * @param column
     * @param value
     * @return
     */
    public Select like(String column, Object value) {
        where(K.AND, column, WK.LIKE, value);
        return this;
    }

    /**
     * like
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select like(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LIKE, value);
        return this;
    }

    public Select like(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LIKE, value);
        return this;
    }


    /**
     * isNull
     *
     * @param column
     * @param value
     * @return
     */
    public Select isNull(String column, Object value) {
        where(K.AND, column, WK.op(WK.IS, WK.NULL), value);
        return this;
    }

    /**
     * isNull
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select isNull(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.IS, WK.NULL), value);
        return this;
    }

    public Select isNull(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.IS, WK.NULL), value);
        return this;
    }


    /**
     * notBetween
     *
     * @param column
     * @param value
     * @return
     */
    public Select notBetween(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    /**
     * between
     * notBetween
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select notBetween(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    public Select notBetween(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    /**
     * notIn
     *
     * @param column
     * @param value
     * @return
     */
    public Select notIn(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }

    /**
     * notIn
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select notIn(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }

    public Select notIn(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }


    /**
     * notLike
     *
     * @param column
     * @param value
     * @return
     */
    public Select notLike(String column, Object value) {
        where(K.AND, column, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }

    /**
     * notLike
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select notLike(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }

    public Select notLike(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }


    /**
     * isNotNull
     *
     * @param column
     * @return
     */
    public Select isNotNull(String column) {
        where(K.AND, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }

    /**
     * isNotNull
     *
     * @param cateNate
     * @param column
     * @return
     */
    public Select isNotNull(String cateNate, String column) {
        where(cateNate, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }

    public Select isNotNull(String cateNate, Express express) {
        where(cateNate, express, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }


    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public Select neq(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.GT), value);
        return this;
    }

    /**
     * 不等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Select neq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.GT), value);
        return this;
    }

    public Select neq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.GT), value);
        return this;
    }

}
