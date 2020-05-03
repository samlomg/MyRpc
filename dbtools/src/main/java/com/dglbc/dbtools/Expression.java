package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.unit.ColumnUnit;
import com.dglbc.dbtools.where.DATEDEPART;
import com.dglbc.dbtools.where.WK;
import com.sun.istack.internal.NotNull;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by LBC on 2017/12/19
 **/
@Accessors(chain = true)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Expression {
    private StringBuilder sql;
    private List values;

    public Expression(@NotNull Column column) {
        this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
        if (StringUtils.isNoneEmpty(column.getBind())) this.sql.append(SQLKey.AS).append(column.getBind());
        this.values = new ArrayList();
    }

    public Expression on(@NotNull Column column){
        this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
        this.values = new ArrayList();
        return this;
    }

    //true 是语句生成的表，false是原生表
    public Expression(@NotNull Table table, @NotNull boolean flag) {
        List temp = new ArrayList();
        if (flag) temp.addAll(table.getValues());
        this.sql = flag ? new StringBuilder().append(SQLKey.LEFT).append(table.getSql()).append(SQLKey.RIGHT).append(table.getAlias()) :
                new StringBuilder().append(table.getName()).append(" ").append(table.getAlias());
        this.values = temp;
    }

    //自定义 语句
    public Expression(@NotNull String sql) {
        this.sql = new StringBuilder().append(sql);
        this.values = new ArrayList();
    }

    public Expression(String clause, List values) {
        this.sql = new StringBuilder().append(sql);
        this.values = values;
    }

    //合并在一起
    public Expression merge(Expression expression) {
        this.sql.append(expression.getSql());
        this.values.addAll(expression.getValues());
        return this;
    }

    //合并在一起
    public Expression merge(String sql, List<Object> objects) {
        this.sql.append(sql);
        this.values.addAll(objects);
        return this;
    }

    //各种函数function获取自身的express 返回的是express
    //首先是叠function 返回的时候expression的情况 叠function 参数数量限制在1
    public Expression op(Expression expression, WK... wks) {
        String opString = WK.op(wks);
        return new Expression(String.format(opString, expression.getSql().toString()), expression.getValues());
    }

    public Expression op(Column column, WK... wks) {
        String opString = WK.op(wks);
        return new Expression(String.format(opString, ColumnUnit.getColumn(column)));
    }

    public Expression op(Table table, String name, WK... wks) {
        String opString = WK.op(wks);
        return new Expression(String.format(opString, table.getAlias() + "." + name));
    }

    //留灵活的方法还是潘多拉盒子系列
    public Expression op(String name, WK... wks) {
        String opString = WK.op(wks);
        return new Expression(String.format(name));
    }

    //时间方面
    public Expression dateAdd(DATEDEPART datepart, int number, Expression expression) {
        return new Expression(String.format(WK.DATEADD.getFormat(), datepart.getC(), number, expression.getSql().toString()), expression.getValues());
    }

    public Expression dateDiff(Expression expression, Expression expression2) {
        expression.getValues().addAll(expression2.getValues());
        return new Expression(
                String.format(WK.DATEDIFF.getFormat(),
                        expression.getSql().toString(),
                        expression2.getSql().toString()
                ), expression.getValues()
        );
    }

    public Expression datePart(DATEDEPART datepart, Expression expression) {
        return new Expression(
                String.format(WK.DATEPART.getFormat(),
                        expression.getSql().toString()
                ), expression.getValues()
        );
    }
}
