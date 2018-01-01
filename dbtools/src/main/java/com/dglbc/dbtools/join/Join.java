package com.dglbc.dbtools.join;

import com.dglbc.dbtools.Expression;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Join implements Serializable {

    private String join;//key world 1:left join,right join ...
    private Expression table; //
    private List<Expression> condition;

    private List parms;
    private StringBuilder sql;

    public Expression builder() {
        StringBuilder sql = new StringBuilder().append(join).append(table.getSql()).append(SqlKey.ON);
        parms.addAll(table.getValues());
        boolean init = false;
        for (Expression expression : condition) {
            if (init) {
                sql.append(SqlKey.AND);
            }
            sql.append(expression.getSql());
            parms.addAll(expression.getValues());
            init = true;
        }
        return new Expression(sql, parms);
    }

    public Join(String join, Expression table) {
        this.join = join;
        this.table = table;
        this.condition = new ArrayList();
        this.parms = new ArrayList();
    }


    public Join(String join, Table table, Column column, Column column2) {
        this.join = join;
        this.table = StringUtils.isEmpty(table.getName()) ? new Expression(table, true) : new Expression(table, false);
        this.condition = new ArrayList<Expression>() {{
            add(new Expression(column.getTable().getAlias() + "." + column.getName() + " = " + column2.getTable().getAlias() + "." + column2.getName()));
        }};
        this.parms = new ArrayList();
    }

    public Join on(Expression expression, Expression expression2) {
        List temp = new ArrayList();
        temp.addAll(expression.getValues());
        temp.addAll(expression2.getValues());
        condition.add(new Expression(expression.getSql().append(" = ").append(expression2.getSql()), temp));
        return this;
    }

    public Join on(Expression expression, Column column) {
        Expression expression1 = new Expression(column, false);
        condition.add(new Expression(expression.getSql().append(" = ").append(expression1.getSql()), expression.getValues()));
        return this;
    }

    public Join on(Column column) {
        StringBuilder temsb = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").
                append(column.getName()).append(" =? ");
        condition.add(new Expression().setSql(temsb).setValues(new ArrayList() {{
            add(column.getValue());
        }}));
        return this;
    }

    public Join on(Expression expression) {
        condition.add(expression);
        return this;
    }
}
