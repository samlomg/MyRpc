package com.dglbc.dbtools;

import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.unit.ColumnUnit;
import com.dglbc.dbtools.where.DATEDEPART;
import com.dglbc.dbtools.where.WK;

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
public class Express {
    private StringBuilder sql;
    private List values;

    private boolean sc;//是否是查询语句

    public Express(StringBuilder sql, List values) {
        this.sql = sql;
        this.values = values;
    }

    public Express( Column column) {
        this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
        if (StringUtils.isNoneEmpty(column.getBind())) this.sql.append(SQLKey.AS).append(column.getBind());
        this.values = new ArrayList();
    }

    public Express on( Column column){
        this.sql = new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
        this.values = new ArrayList();
        return this;
    }

    //true 是语句生成的表，false是原生表
    public Express( Table table,  boolean flag) {
        List temp = new ArrayList();
        if (flag) temp.addAll(table.getValues());
        this.sql = flag ? new StringBuilder().append(SQLKey.LEFT).append(table.getSql()).append(SQLKey.RIGHT).append(table.getAlias()) :
                new StringBuilder().append(table.getName()).append(" ").append(table.getAlias());
        this.values = temp;
    }

    //自定义 语句
    public Express( String sql) {
        this.sql = new StringBuilder().append(sql);
        this.values = new ArrayList();
    }

    public Express(String clause, List values) {
        this.sql = new StringBuilder().append(sql);
        this.values = values;
    }

    //合并在一起
    public Express merge(Express express) {
        this.sql.append(express.getSql());
        this.values.addAll(express.getValues());
        return this;
    }

    //合并在一起
    public Express merge(String sql, List<Object> objects) {
        this.sql.append(sql);
        this.values.addAll(objects);
        return this;
    }

    //各种函数function获取自身的express 返回的是express
    //首先是叠function 返回的时候expression的情况 叠function 参数数量限制在1
    public Express op(Express express, WK... wks) {
        String opString = WK.op(wks);
        return new Express(String.format(opString, express.getSql().toString()), express.getValues());
    }

    public Express op(Column column, WK... wks) {
        String opString = WK.op(wks);
        return new Express(String.format(opString, ColumnUnit.getColumn(column)));
    }

    public Express op(Table table, String name, WK... wks) {
        String opString = WK.op(wks);
        return new Express(String.format(opString, table.getAlias() + "." + name));
    }

    //留灵活的方法还是潘多拉盒子系列
    public Express op(String name, WK... wks) {
        String opString = WK.op(wks);
        return new Express(String.format(name));
    }

    //时间方面
    public Express dateAdd(DATEDEPART datepart, int number, Express express) {
        return new Express(String.format(WK.DATEADD.getFormat(), datepart.getC(), number, express.getSql().toString()), express.getValues());
    }

    public Express dateDiff(Express express, Express express2) {
        express.getValues().addAll(express2.getValues());
        return new Express(
                String.format(WK.DATEDIFF.getFormat(),
                        express.getSql().toString(),
                        express2.getSql().toString()
                ), express.getValues()
        );
    }

    public Express datePart(DATEDEPART datepart, Express express) {
        return new Express(
                String.format(WK.DATEPART.getFormat(),
                        express.getSql().toString()
                ), express.getValues()
        );
    }
}
