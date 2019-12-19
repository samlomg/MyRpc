package com.dglbc.dbtools.unit;

import com.dglbc.dbtools.run.TipsShow;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.WK;

import java.util.List;

public class WKUnit {
    //
    public static StringBuilder getColumn(Column column) {
        return new StringBuilder().append(" ").append(column.getTable().getAlias()).append(".").append(column.getName()).append(" ");
    }

    //
    public static StringBuilder getColumn(Table table, String name) {
        return new StringBuilder().append(" ").append(table.getAlias()).append(".").append(name).append(" ");
    }

    //whereString
    public static StringBuilder getWS(Table table, String name,String operation) {
        return new StringBuilder().append(" ").append(table.getAlias()).append(".").append(name).append(" ").append(operation);
    }

    //where operation 正常情况下
    public static String getOperation(String operation, List values){
        StringBuilder op = new StringBuilder();
        if (operation.indexOf("BETWEEN") > -1 && values.size() ==2){
            //between
            op.append(operation).append(" ? AND ?");
        }else if (operation.indexOf("IN") > -1){
            op.append(operation).append(" (");
            StringBuilder question = new StringBuilder();
            values.forEach(q -> {question.append(",?");});
            op.append(question.substring(1)).append(" ) ");
        }else if (values.size() == 1){
            op.append(operation).append("?");
        }else {
            TipsShow.alert("操作符号请按标准的输入");
        }
        return op.toString();
    }

}
