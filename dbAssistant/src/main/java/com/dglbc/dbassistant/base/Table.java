package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.Unitls;
import javafx.scene.Parent;
import lombok.*;
import lombok.experimental.Accessors;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/29 13:05
 * 暂时不考虑检查的细节。
 * 以后可能会考虑把加入的column名称检查
 */

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Table extends Express implements AbstractExpress {

    private String tableName;//表名（如果是外联直接在表名体现把，不再新增特别的字段记录）
    private String alias;//简称
    private boolean needFrom = false;//

    public Table(String table) {
        this.tableName = table;
    }

    public Table(String tableName, boolean needFrom) {
        this.tableName = tableName;
        this.needFrom = needFrom;
    }

    public Table(String tableName, String alias) {
        this.tableName = tableName;
        this.alias = alias;
    }

    @Override
    public Express toExpress() throws Exception {
        //todo 这里如果表名就含有select 就会发生bug。如果要彻底清理必须另外想办法。
        if (super.sec() && (sql().toString().toUpperCase().indexOf("SELECT") > -1 && sql().toString().toUpperCase().indexOf("FROM") > -1)) {
            //检查是否是多次build的情况
            if (sql().indexOf(" (") != 0)
                //处理用查询语句做表的情况
                super.sql().append(needFrom ? K.FROM:"").insert(0, " ( ").append(" ) ").append(alias).append(" ");
        } else {
            clear();
            super.sql().append(needFrom ? K.FROM:"");
            //提供一种默认的方式输入express（直接返回一个table的）
            if (tableName == null || tableName.isEmpty() || tableName.trim().length() <= 0) {
                TipsShow.alert("请把表名填写完整");
            } else {
                super.sql().append(" ").append(tableName);
            }
            //这是一个
            if (alias == null || alias.isEmpty() || alias.trim().length() <= 0) {

            } else {
                super.sql().append(" ").append(alias);
            }
            super.sql().append(K.WITH);
        }
        this.sec(true);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(tableName)) {
            re.code(10001).status("Table check Fail!");
        }
        return re;
    }
}
