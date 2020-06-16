package com.dglbc.dbtools.join;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.SQLKey;
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
    private Express table; //
    private List<Express> condition;

    private List parms;
    private StringBuilder sql;

    public Express builder() {
        StringBuilder sql = new StringBuilder().append(join).append(table.getSql()).append(SQLKey.ON);
        parms.addAll(table.getValues());
        boolean init = false;
        for (Express express : condition) {
            if (init) {
                sql.append(SQLKey.AND);
            }
            sql.append(express.getSql());
            parms.addAll(express.getValues());
            init = true;
        }
        return new Express(sql, parms);
    }

    public Join(String join, Express table) {
        this.join = join;
        this.table = table;
        this.condition = new ArrayList();
        this.parms = new ArrayList();
    }

    public Join(String join, Table table) {
        this.join = join;
        this.table = StringUtils.isEmpty(table.getName()) ? new Express(table, true) : new Express(table, false);
        this.condition = new ArrayList<>();
        this.parms = new ArrayList();
    }

    public Join(String join, Table table, final Column column, final Column column2) {
        this.join = join;
        this.table = StringUtils.isEmpty(table.getName()) ? new Express(table, true) : new Express(table, false);
        this.condition = new ArrayList<Express>() {{
            add(new Express(column.getTable().getAlias() + "." + column.getName() + " = " + column2.getTable().getAlias() + "." + column2.getName()));
        }};
        this.parms = new ArrayList();
    }

    public Join on(Express express, Express express2) {
        List temp = new ArrayList();
        temp.addAll(express.getValues());
        temp.addAll(express2.getValues());
        condition.add(new Express(express.getSql().append(" = ").append(express2.getSql()), temp));
        return this;
    }



    public Join on(Express express, Column column) {
        Express express1 = new Express(column);
        condition.add(new Express(express.getSql().append(" = ").append(express1.getSql()), express.getValues()));
        return this;
    }

    public Join on(Column column, Column column2) {
        Express express = new Express(column);
        Express express1 = new Express(column2);

        List templist = new ArrayList();
        templist.addAll(express.getValues());
        templist.addAll(express1.getValues());
        condition.add(new Express(express.getSql().append(" = ").append(express1.getSql()), templist));
        return this;
    }

    public Join on(final Column column) {
        StringBuilder temsb = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").
                append(column.getName()).append(" =? ");
        condition.add(new Express().setSql(temsb).setValues(new ArrayList() {{
            add(column.getValue());
        }}));
        return this;
    }

    public Join on(Express express) {
        condition.add(express);
        return this;
    }
}
