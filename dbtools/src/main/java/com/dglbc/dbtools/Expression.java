package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
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

    public Expression(Column column, boolean flag) {
        if (flag) {
            this.sql = new StringBuilder().append("?");
            this.values = new ArrayList() {{
                add(column.getValue());
            }};
        } else {
            this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
            this.values = new ArrayList();
        }
    }

    //true 是语句生成的表，false是原生表
    public Expression(Table table, boolean flag) {
        List temp = new ArrayList();
        if (flag) temp.addAll(table.getValues());
        this.sql = flag ? new StringBuilder().append(SqlKey.LEFT).append(table.getSql()).append(SqlKey.RIGHT).append(table.getAlias()) :
                new StringBuilder().append(table.getName()).append(" ").append(table.getAlias());
        this.values = temp;
    }


    public Expression(String sql) {
        this.sql = new StringBuilder().append(sql);
        this.values = new ArrayList();
    }

    //datepart, number, expression
    public static Expression dateAdd(String datepart, String number, Expression expression) {
//        return new Expression(" DATEADD ( "+datepart+","+number+","+expression.getSql()+" ) ",expression.getValues());
        return new Expression(new StringBuilder().append(SqlKey.DATEADD).append(SqlKey.LEFT).append(datepart).
                append(",").append(number).append(",").append(expression.getSql()).append(SqlKey.RIGHT), expression.getValues());
    }

    public static Expression dateAdd(String time, String datepart, String number) {
        return dateAdd(datepart, number, new Expression(time));
    }

    public static Expression isnull(Expression expression, Object value) {
        List templist = new ArrayList();
        templist.addAll(expression.getValues());
        templist.add(value);
        return new Expression(new StringBuilder().append(SqlKey.ISNULL).append(SqlKey.LEFT).
                append(expression.getSql()).append(",?").append(SqlKey.RIGHT), templist);
    }

    public static Expression isnull(Column column) {
        return isnull(new Expression(column, false), column.getValue());
    }

}
