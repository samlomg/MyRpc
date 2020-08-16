package com.dglbc.dbassistant.unitils;


import com.dglbc.dbassistant.tips.TipsShow;

import java.util.Arrays;
import java.util.List;

public class WKUnit {

    //where operation 正常情况下
    public static String getOperation(String operation, Object... values){
        StringBuilder op = new StringBuilder();
        if (operation.indexOf("BETWEEN") > -1 && values.length ==2){
            //between
            op.append(String.format(operation, "?","?"));
        }else if (operation.indexOf("IN") > -1){
            StringBuilder question = new StringBuilder();
            Arrays.asList(values).forEach(q -> {question.append(",?");});
            op.append(String.format(operation, question.substring(1)));
        }else if (values.length == 1){
            op.append(String.format(operation, "?"));
        }else if (values.length == 0){
            op.append(operation);
        }else {
            TipsShow.alert("操作符号请按标准的输入");
        }
        return op.toString();
    }

}
