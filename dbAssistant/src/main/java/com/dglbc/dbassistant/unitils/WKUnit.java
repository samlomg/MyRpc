package com.dglbc.dbassistant.unitils;


import com.dglbc.dbassistant.tips.TipsShow;

import java.util.List;

public class WKUnit {

    //where operation 正常情况下
    public static String getOperation(String operation, List values){
        StringBuilder op = new StringBuilder();
        if (operation.indexOf("BETWEEN") > -1 && values.size() ==2){
            //between
            op.append(String.format(operation, "?","?"));
        }else if (operation.indexOf("IN") > -1){
            StringBuilder question = new StringBuilder();
            values.forEach(q -> {question.append(",?");});
            op.append(String.format(operation, question.substring(1)));
        }else if (values.size() == 1){
            op.append(String.format(operation, "?"));
        }else if (values.size() == 0){
            op.append(operation);
        }else {
            TipsShow.alert("操作符号请按标准的输入");
        }
        return op.toString();
    }

}
