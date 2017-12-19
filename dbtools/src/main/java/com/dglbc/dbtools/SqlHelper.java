package com.dglbc.dbtools;

import com.dglbc.dbtools.join.Join;
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

@Accessors(chain = true)
@Setter
@Getter
public class SqlHelper implements Serializable {

    private List<String> selectContent;
    private List<Unit> insertContent;
    private List<Unit> updateContent;
    private List<Where> conditions;
    private String table;
    private List<Join> joins;

    private String order;

    public SqlHelper(String table) {
        this.table = table;
        this.selectContent = new ArrayList<>();
        this.insertContent = new ArrayList<>();
        this.updateContent = new ArrayList<>();
        this.conditions = new ArrayList<>();
        this.joins = new ArrayList<>();
    }

    //查询语句
    public SqlHelper sc(String name) {
        this.selectContent.add("A." + name + " AS " + name.toUpperCase());
        return this;
    }

    //查询语句
    public SqlHelper sc(Join alias, String name) {
        this.selectContent.add(alias.getAlias() + "." + name + " AS " + name.toUpperCase());
        return this;
    }

    //查询语句
    public SqlHelper sc(Join alias, String name, String aname) {
        this.selectContent.add(alias.getAlias() + "." + name + " AS " + aname);
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

    public SqlHelper addJoin(Join join){
        joins.add(join);
        return this;
    }

    public SqlHelper addWhere(Where where){
        conditions.add(where);
        return this;
    }

    //生成 查询语句 首先
    // 1：把selectContent里面的内容生成出来
    // 2：生成表信息
    // 3：生成join的信息
    // 4：生成where的信息
    // 5：生成group by order by 信息
    public ExecSql selectBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SqlKey.SELECT);
        sql.append(selectContent.toString().replaceAll("[\\[\\]]", " "));
        sql.append(SqlKey.FROM).append(table).append(" A").append(SqlKey.WITH);
        if (joins.size() > 0) {
            for (Join join : joins) {
                ExecSql tempsql = join.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }
        }
        sql.append(SqlKey.WHERE);
        if (conditions.size() > 0) {
            for (Where where : conditions) {
                ExecSql tempsql = where.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }
        }
        return new ExecSql(sql.toString(), params);
    }


}
