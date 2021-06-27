package com.dglbc.dbassistant.dml;


import com.dglbc.dbassistant.annotation.Ignore;
import com.dglbc.dbassistant.annotation.MyColumn;
import com.dglbc.dbassistant.annotation.MyTable;
import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * warning : 致喜欢jdbc的志同道合之人
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
public class Select extends Condition<Select>  implements DML {
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
     * 简化group by 和having的操作，直接自己写。这样的好处是不用
     * 提供工具自行转换。
     */
    private OtherExpress others;

    public Express build() {
        if (sec()) clear();
        this.sql().append(K.SELECT);
        checkParts(first);
        checkParts(columns);
        checkParts(table);
        checkParts(joins);
        checkParts(wheres());
        checkParts(others);
        //重要一点如果是已经构建生成的express必须有标志
        this.sec(true);
        return this;
    }

    /**
     * sqlServer 2005 以后能用的分页
     *
     * @param size
     * @param page
     * @param key
     * @return
     */
    public Express pageSQLServerOld(int size, int page, String key) {
        if (sec()) clear();
        sql().append("SELECT Top ? *  FROM ( SELECT ROW_NUMBER() OVER(Order by ").append(key).append(") AS RowId,");
        values().add(size);
        checkParts(first);
        checkParts(columns);
        checkParts(table);
        checkParts(joins);
        checkParts(wheres());
        checkParts(others);
        sql().append(") A  WHERE RowId between ? and ? ");
        values().add((page - 1) * size + 1);
        values().add(page * size);
        return this;
    }

    @Override
    public Select me() {
        return this;
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
        this.table = new Table(tab, true);
        //todo 目前没想到什么好的办法只能先new然后赋值
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres(new Where(specialExpress)) ;
    }

    //补充情况
    public Select(String col, String tab) {
        this.columns = new Column(new Express(col));
        this.table = new Table(tab, true);
    }

    public Select(String sqlAll) {
        this.first = new FirstExpress(sqlAll);
    }

    /*
    * 210613 需要达到的效果
    * 1:放入class 自动生成column
    * 2：自动生成的column排除部分数据库没有的
    *
    * */
    public Select(Class  cl){

        //table
        if (cl.isAnnotationPresent(MyTable.class)){
            MyTable myTable= (MyTable) cl.getAnnotation(MyTable.class);
            this.table = new Table(myTable.tableName(), myTable.alias(),true);
        }else {
            this.table = new Table(cl.getSimpleName(), "t_"+cl.getSimpleName().toLowerCase(),true);
        }

        this.columns = new Column();
        Field[] fields = cl.getDeclaredFields();
        for (Field field:fields){
            field.setAccessible(true);
            if (field.isAnnotationPresent(Ignore.class)){
                continue;
            }
            if (field.isAnnotationPresent(MyColumn.class)){
                MyColumn myColumn= field.getAnnotation(MyColumn.class);
                column(myColumn.columnName(),myColumn.as());
            }else {
                column(field.getName());
            }
        }

    }

    /**
     * 分割线下面是为了方便增加的工具方法不是core
     * ==================================
     */

    public static Select create() {
        return new Select();
    }

    public Select column(String columns, Object... values) {
        if (columns == null || columns.trim().equals("")) return this;
        if (this.columns() == null) this.columns = new Column();
        this.columns.columns().add(values.length == 0 ? new Express(columns) : new Express(columns, Arrays.asList(values)));
        return this;
    }

    public Select column(String columns, String as, Object... values) {
        if (this.columns() == null) this.columns = new Column();
        this.columns.columns().add(values.length == 0 ? new Express(columns + K.AS + as) : new Express(columns + K.AS + as, Arrays.asList(values)));
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

    public Select from(String table, String alias) {
        if (table() == null) {
            this.table = new Table(table, alias, true);
        } else {
            this.table = this.table.tableName(table).alias(alias);
        }
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

    public Select join(String tab, String alias, String on) {
        if (joins == null) joins = new Join();

        this.joins.joins().add(new ExpressWithTable(tab + " " + alias, on));

        return this;
    }

    public Select leftJoin(String tab, String alias, String on) {
        join(tab, alias, on);
        return this;
    }

    public Select leftJoin(Express table, String alias, String on) {
//        join(tab, alias, on);
        if (joins == null) joins = new Join();
        this.joins.joins().add(new ExpressWithTable(table, alias, on));
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

    public Select innerJoin(String tab, String alias, String on) {
        if (joins == null) {
            joins = new Join();
        }
        this.joins.joins().add(new ExpressWithTable(K.INNERJOIN, tab + " " + alias, on));
        return this;
    }

    public Select innerJoin(Express table, String alias, String on) {
        if (joins == null) joins = new Join();
        this.joins.joins().add(new ExpressWithTable(K.INNERJOIN, table, alias, on));
        return this;
    }

    public Select last(String by) {
        if (this.others == null) {
            this.others = new OtherExpress();
        }
        others.others().add(new Express(by));
        return this;
    }

}
