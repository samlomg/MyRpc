package com.dglbc.dbtools;

import com.dglbc.dbtools.annotation.FilterCondition;
import com.dglbc.dbtools.annotation.NotInclude;
import com.dglbc.dbtools.declear.CrudOperate;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.produce.ParameterMode;
import com.dglbc.dbtools.produce.ProduceParameter;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.unit.ClassGenSQL;
import com.dglbc.dbtools.where.Where;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 目标是返回一个string sql，还有一个参数数组
 */

@SuppressWarnings("ALL")
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class SQLHelper implements Serializable {
    private boolean order = false;
    private boolean group = false;
    private boolean have = false;
    private Table table;//主表
    private String produceFuntion;

    private List<Express> selectContent = new ArrayList<>();
    private List<String> specials = new ArrayList<>();
    private List<Column> insertContent = new ArrayList<>();
    private List<Column> updateContent = new ArrayList<>();
    private List<Where> conditions = new ArrayList<>();
    private List<Join> joins = new ArrayList<>();
    private List<Column> groupContent = new ArrayList<>();
    private List<Column> orderContent = new ArrayList<>();
    private List<Where> havingConditions = new ArrayList<>();
    private List<ProduceParameter> produceParameters = new ArrayList<>();

    public SQLHelper(Table table) {
        this.table = table;
    }

    public SQLHelper as(String special) {
        this.specials.add(special);
        return this;
    }

    //存储过程
    public SQLHelper call(String sql) {
        this.produceFuntion = sql;
        this.produceParameters = new ArrayList<>();
        return this;
    }

    public SQLHelper cc(ParameterMode parameterMode, int num, Object parm) {
        produceParameters.add(new ProduceParameter(num, parameterMode, parm));
        return this;
    }

    public SQLHelper cc(int num, Object parm) {
        produceParameters.add(new ProduceParameter(num, ParameterMode.IN, parm));
        return this;
    }

    public SQLHelper cc(Object parm) {
        produceParameters.add(new ProduceParameter(ParameterMode.IN, parm));
        return this;
    }

    //查询语句,自定义
    public SQLHelper sc(String name) {
        return sc(new Column(table, name));
    }

    public SQLHelper sc(Table table, String name) {
        return sc(new Column(table, name));
    }

    public SQLHelper sc(Table table, String name, String bind) {
        return sc(new Column(table, name, bind));
    }

    public SQLHelper sc(Column column) {
        return sc(new Express(column));
    }

    public SQLHelper sc(Express express) {
        this.selectContent.add(express);
        return this;
    }

    //更新语句
    public SQLHelper uc(Column column) {
        this.updateContent.add(column);
        return this;
    }

    //插入语句
    public SQLHelper ic(Column column) {
        this.insertContent.add(column);
        return this;
    }

    //joiin
    public SQLHelper join(Join join) {
        joins.add(join);
        return this;
    }

    //where条件
    public SQLHelper where(Where where) {
        conditions.add(where);
        return this;
    }

    public SQLHelper where(List<Where> wheres) {
        conditions.addAll(wheres);
        return this;
    }


    //充分灵活性（灵活性max）潘多拉的盒子。正确与否全凭自己的能力察觉
    public SQLHelper where(String logic,String clause,List values){
        conditions.add(new Where(logic,() -> {return new Express(clause,values);}));
        return this;
    }

    public SQLHelper where(String clause,List values){
        conditions.add(new Where(() -> {return new Express(clause,values);}));
        return this;
    }

    public SQLHelper where(String logic,String clause,Object value){
        conditions.add(new Where(logic,() -> {return new Express(clause,Arrays.asList(value));}));
        return this;
    }

    public SQLHelper where(String clause,Object value){
        conditions.add(new Where(() -> {return new Express(clause,Arrays.asList(value));}));
        return this;
    }

    public SQLHelper where(String logic,String clause){
        conditions.add(new Where(logic,() -> {return new Express(clause);}));
        return this;
    }
    public SQLHelper where(String clause){
        conditions.add(new Where(() -> {return new Express(clause);}));
        return this;
    }

    //常规having groupBy orderBy
    public SQLHelper having(Where where) {
        this.group = true;
        this.have = true;
        havingConditions.add(where);
        return this;
    }

    /*
        Group by 是按select的内容，毕竟group by 和select 是要相同，对了自定义部分没有自动列入。
     */
    public SQLHelper groupBy(Column column) {
        this.group = true;
        groupContent.add(column);
        return this;
    }

    public SQLHelper orderBy(Column column) {
        this.order = true;
        orderContent.add(column);
        return this;
    }

    //生成builder
    /*
        生成 查询语句 首先
        1：把selectContent里面的内容生成出来
        2：生成表信息
        3：生成join的信息
        4：生成where的信息
        5：生成group by order by 信息
     */
    public Express selectBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQLKey.SELECT);
        for (String special : specials) sql.append(special).append(" ");
        StringBuilder tem1 = new StringBuilder();
        for (Express express : selectContent) {
            tem1.append(",").append(express.getSql());
            params.addAll(express.getValues());
        }
        sql.append(tem1.delete(0, 1)).append(SQLKey.FROM).append(table.getName()).append(" ").append(table.getAlias()).append(SQLKey.WITH);
        if (joins.size() > 0) {
            for (Join join : joins) {
                Express tempsql = join.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }
        }
        sql.append(SQLKey.WHERE);

        if (conditions.size() > 0) {
            for (Where where : conditions) {
                Express tempsql = where.builder();
                sql.append(tempsql.getSql());
                params.addAll(tempsql.getValues());
            }
        }

        if (group) {
            sql.append(SQLKey.GROUP);
            String temp = new String();
            for (Column column : groupContent) temp += "," + column.getTable().getAlias() + "." + column.getName();
            sql.append(temp.replaceFirst(",", ""));

            if (have) {
                sql.append(SQLKey.HAVING);
                if (havingConditions.size() > 0) {
                    for (Where where : havingConditions) {
                        Express tempsql = where.builder();
                        sql.append(tempsql.getSql());
                        params.addAll(tempsql.getValues());
                    }
                }
            }
        }

        if (order) {
            sql.append(SQLKey.ORDER);
            String temp1 = new String();
            for (Column column : orderContent) temp1 += "," + column.getTable().getAlias() + "." + column.getName();
            sql.append(temp1.replaceFirst(",", ""));
        }
        return new Express(sql, params);
    }

    /*
       插入语句生成器
     */
    public Express insertBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQLKey.INSERT).append(table.getName()).append(" ( ");
        StringBuilder sql1 = new StringBuilder();
        StringBuilder sql2 = new StringBuilder();
        for (Column column : insertContent) {
            sql2.append(",").append(column.getName());
            sql1.append(",?");
            params.add(column.getValue());
        }
        sql.append(sql2.delete(0, 1)).append(" ) ").append(SQLKey.VALUES).append(" ( ").append(sql1.delete(0, 1)).append(" ) ");
        return new Express(sql, params);
    }

    /*
        更新语句生成器
    */
    public Express updateBuilder() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQLKey.UPDATE).append(table.getName()).append(SQLKey.SET);
        StringBuilder sql1 = new StringBuilder();
        for (Column column : updateContent) {
            sql1.append(",").append(column.getName()).append(" =? ");
            params.add(column.getValue());
        }
        sql.append(sql1.delete(0, 1)).append(SQLKey.WHERE);
        for (Where where : conditions) {
            Express tempsql = where.builder();
            sql.append(tempsql.getSql());
            params.addAll(tempsql.getValues());
        }
        return new Express(sql, params);
    }

    /*
        删除语句生成器
    */
    public Express deleteBulider() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder(SQLKey.DELETE).append(" ").append(table.getAlias()).append(" ")
                .append(SQLKey.FROM).append(table.getName()).append(" ").append(table.getAlias()).append(" ")
                .append(SQLKey.WHERE);
        for (Where where : conditions) {
            Express tempsql = where.builder();
            sql.append(tempsql.getSql());
            params.addAll(tempsql.getValues());
        }
        return new Express(sql, params);
    }

    /*
        过程语句生成器
     */
    public Express callBulider() {
        List<Object> params = new ArrayList<>();
        StringBuilder sql = new StringBuilder().append(" { ").append(produceFuntion).append(SQLKey.LEFT);
        StringBuilder tem1 = new StringBuilder();
        for (ProduceParameter produceParameter : produceParameters) {
            tem1.append(",?");
            params.add(produceParameter);
        }
        sql.append(tem1.delete(0, 1)).append(SQLKey.RIGHT).append(" } ");
        return new Express(sql, params);
    }

    //自动化生成的辅助
    public SQLHelper(Class cl) {
        this.table = new Table(cl.getSimpleName(), cl.getSimpleName() + "_l");
        select(cl.getDeclaredFields());
    }

    public SQLHelper(Class cl, Object o, CrudOperate... crudOperates) throws IllegalAccessException {
        this.table = new Table(cl.getSimpleName(), cl.getSimpleName() + "_b");
        Field[] fields = cl.getDeclaredFields();
        for (int i = 0; i < crudOperates.length; i++) {
            if (crudOperates[i].equals(CrudOperate.INSERT)) insert(fields, o);
            if (crudOperates[i].equals(CrudOperate.UPDATE)) update(fields, o);
        }
    }

    /*
        生成sqlhelper
    */
    public SQLHelper insert(Field[] fields, Object o) throws IllegalAccessException {
        //首先获取class的属性 来生成sql语句
        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(NotInclude.class)) {
                this.ic(new Column(table, field.getName(), field.get(o)));
            }
        }
        return this;
    }

    public SQLHelper update(Field[] fields, Object o) throws IllegalAccessException {
        //首先获取class的属性 来生成sql语句

        SQLHelper sqlHelper = new SQLHelper(table);

        for (Field field : fields) {
            field.setAccessible(true);
            if (!field.isAnnotationPresent(NotInclude.class)) {
                this.uc(new Column(table, field.getName(), field.get(o)));
            }
            if (field.isAnnotationPresent(FilterCondition.class)) {
                this.where(field.getName(),new Column(table, field.getName(), field.get(o)));
            }

        }

        return this;
    }

    public SQLHelper update(Field[] fields, Object o, String key) throws IllegalAccessException {
        //首先获取class的属性 来生成sql语句

        SQLHelper sqlHelper = new SQLHelper(table);
        key = key == null ? "sequence" : key;
        Field seq = null;
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(key)) {
                seq = field;
            } else {
                this.uc(new Column(table, field.getName(), field.get(o)));
            }
        }

        return this;
    }

    public SQLHelper select(Field[] fields) {
        //首先获取class的属性 来生成sql语句
        for (Field field : fields) {
            this.sc(table, field.getName());
        }
        return this;
    }

    public SQLHelper merge(ClassGenSQL classGenSQL) {
        selectContent.addAll(classGenSQL.getSelectContent().values());
        return this;
    }

}
