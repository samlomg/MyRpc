package com.dglbc.dbtools;

import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 目标是返回一个string sql，还有一个参数数组
 */

@SuppressWarnings("ALL")
@Accessors(chain = true)
@Setter
@Getter
public class SqlHelper implements Serializable {

    private List<Expression> selectContent;
    private List<Column> insertContent;
    private List<Column> updateContent;
    private List<Where> conditions;
    private Table table;
    private List<Join> joins;
    private boolean order = false;
    private boolean group = false;
    private boolean have = false;
    private List<Column> groupContent;
    private List<Column> orderContent;
    private List<Where> havingConditions;

    public SqlHelper(Table table) {
        this.table = table;
        this.selectContent = new ArrayList<>();
        this.insertContent = new ArrayList<>();
        this.updateContent = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.joins = new ArrayList<>();
        this.groupContent = new ArrayList<>();
        this.orderContent = new ArrayList<>();
        this.havingConditions = new ArrayList<>();
    }

    //查询语句,自定义
    public SqlHelper sc(Table table,String name) {
        return sc(new Column(table,name));
    }

    //查询语句,自定义
    public SqlHelper sc(Column column) {
        return sc(new Expression(column, false));
    }

    //查询语句
    public SqlHelper sc(Expression expression) {
        this.selectContent.add(expression);
        return this;
    }

    //更新语句
    public SqlHelper uc(Column column) {
        this.updateContent.add(column);
        return this;
    }

    //插入语句
    public SqlHelper ic(Column column) {
        this.insertContent.add(column);
        return this;
    }

    public SqlHelper join(Join join) {
        joins.add(join);
        return this;
    }

    public SqlHelper where(Where where) {
        conditions.add(where);
        return this;
    }

    public SqlHelper having(Where where) {
        this.group = true;
        this.have = true;
        havingConditions.add(where);
        return this;
    }

    /*
        Group by 是按select的内容，毕竟group by 和select 是要相同，对了自定义部分没有自动列入。
     */
    public SqlHelper groupBy() {
        this.group = true;
        return this;
    }

    public SqlHelper orderBy(Column column) {
        this.order = true;
        orderContent.add(column);
        return this;
    }

    /*
    生成 查询语句 首先
    1：把selectContent里面的内容生成出来
    2：生成表信息
    3：生成join的信息
    4：生成where的信息
    5：生成group by order by 信息
     */
    public Expression selectBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SqlKey.SELECT);
//        sql.append(selectContent.toString().replaceAll("[\\[\\]]", " "));
        for (Expression expression : selectContent) {
            sql.append(expression.getSql());
            params.addAll(expression.getValues());
        }
        sql.append(SqlKey.FROM).append(table.getName()).append(" ").append(table.getAlias()).append(SqlKey.WITH);
        if (joins.size() > 0) {
            for (Join join : joins) {
                Expression tempsql = join.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }

        }
        sql.append(SqlKey.WHERE);

        if (conditions.size() > 0) {
            for (Where where : conditions) {
                Expression tempsql = where.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }
        }

        if (group) {
            sql.append(SqlKey.GROUP);
            String temp = new String();
            for (Column column : groupContent) {
                temp += "," + column.getTable().getAlias() + "." + column.getName();
            }
            sql.append(temp.replaceFirst(",", ""));

            if (have) {
                sql.append(SqlKey.HAVING);

                if (havingConditions.size() > 0) {
                    for (Where where : havingConditions) {
                        Expression tempsql = where.builder();
                        sql.append(tempsql.getSql());
                        params.addAll(tempsql.getValues());
                    }
                }
            }
        }
        
        if (order) {
            sql.append(SqlKey.ORDER);
            String temp1 = new String();
            for (Column column : orderContent) {
                temp1 += "," + column.getTable().getAlias() + "." + column.getName();
            }
            sql.append(temp1.replaceFirst(",", ""));
        }
        return new Expression(sql, params);
    }

    /*
       插入语句生成器
     */
    public Expression insertBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SqlKey.INSERT).append(table.getName()).append(" ( ");
        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        for (Column column : insertContent) {
            sql2.append(",").append(column.getName());
            sql1.append(",?");
            params.add(column.getValue());
        }
        sql.append(sql2.delete(0, 1)).append(" ) ").append(SqlKey.VALUES).append(" ( ").append(sql1.delete(0, 1)).append(" ) ");
        return new Expression(sql, params);
    }

    /*
        更新语句生成器
    */
    public Expression updateBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SqlKey.UPDATE).append(table.getName()).append(SqlKey.SET);
        StringBuilder sql1 = new StringBuilder();
        for (Column column : updateContent) {
            sql1.append(",").append(column.getName()).append(" =? ");
            params.add(column.getValue());
        }

        sql1.delete(0, 1);
        sql.append(sql1).append(SqlKey.WHERE);
        for (Where where : conditions) {
            Expression tempsql = where.builder();
            sql.append(tempsql.getSql());
            params.addAll(tempsql.getValues());
        }
        return new Expression(sql, params);
    }

    /*
        删除语句生成器
    */
    public Expression deleteBulider() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SqlKey.DELETE).append(" ").append(table.getAlias()).append(" ")
                .append(SqlKey.FROM).append(table.getName()).append(" ").append(table.getAlias()).append(" ").append(SqlKey.WHERE);
        for (Where where : conditions) {
            Expression tempsql = where.builder();
            sql.append(tempsql.getSql());
            params.addAll(tempsql.getValues());
        }
        return new Expression(sql, params);
    }

}
