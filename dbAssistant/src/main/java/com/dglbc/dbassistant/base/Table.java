package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.tips.TipsShow;
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

    private String table;//表名
    private String alias;//简称
    private String prefix;//可能是外联表（这里是给严格按照游戏规则人使用）


    @Override
    public Express toExpress() throws Exception  {
        if (super.sec()) {
            //处理用查询语句做表的情况
            super.sql().insert(0, " ( ").append(" ) ").append(alias).append(" ");
        } else {
            //提供一种默认的方式输入express（直接返回一个table的）
            if (table == null || table.isEmpty() || table.trim().length() <= 0) {
                TipsShow.alert("请把表名填写完整");
            }
            if (alias == null || alias.isEmpty() || alias.trim().length() <= 0) {
                TipsShow.alert("请把表名简称填写完整");
            }
            super.sql().append(table).append(" ").append(alias);
        }
        return this;
    }


}
