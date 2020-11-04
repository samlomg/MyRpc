package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/31 22:58
 */
@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Join extends Express implements AbstractExpress {

    private List<ExpressWithTable> joins = new ArrayList<>();

    @Override
    public Express toExpress() throws Exception {
        if (sec()) clear();
        this.mergeJoins(joins);
        this.sec(true);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(joins)){
            re.code(10001).status("Join check Fail!");
        }
        return re;
    }

    Express mergeJoins(List<ExpressWithTable> columns) {
        boolean isFirst = true;
        columns.forEach(express -> {
            try {
                this.merge(express.toExpress());
                this.sql().append(" ");
            } catch (Exception e) {
                e.printStackTrace();
                TipsShow.alert("合并错误！");
            }
        });
        return this;
    }
}
