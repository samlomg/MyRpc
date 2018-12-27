package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import lombok.*;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

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

    public Expression(final Column column, boolean flag) {
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

    /*
     * 查询语句专用
     * */
    public Expression(Column column) {
        this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
        if (StringUtils.isNoneEmpty(column.getBind())) this.sql.append(SQLKey.AS).append(column.getBind());
        this.values = new ArrayList();
    }

    //true 是语句生成的表，false是原生表
    public Expression(Table table, boolean flag) {
        List temp = new ArrayList();
        if (flag) temp.addAll(table.getValues());
        this.sql = flag ? new StringBuilder().append(SQLKey.LEFT).append(table.getSql()).append(SQLKey.RIGHT).append(table.getAlias()) :
                new StringBuilder().append(table.getName()).append(" ").append(table.getAlias());
        this.values = temp;
    }

    //自定义 语句
    public Expression(String sql) {
        this.sql = new StringBuilder().append(sql);
        this.values = new ArrayList();
    }

    public Expression merge(Expression expression){
        this.sql.append(expression.getSql());
        this.values.addAll(expression.getValues());
        return this;
    }

}
