package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Column extends Express implements AbstractExpress {

    private List<Express> columns = new ArrayList<>();

    public Column(Express column) {
        this.columns.add(column);
    }

    @Override
    public Express toExpress() throws Exception {
        if (columns == null || columns.size() == 0) {
            TipsShow.alert("SQL查询语句必须有查询的项");
        }

        mergeColumns(columns);
        return this;
    }
    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(columns)){
            re.code(10001).status("Column check Fail!");
        }
        return re;
    }

    Express mergeColumns(List<Express> columns) {
        boolean isFirst = true;
        columns.forEach(express -> {
            if (isFirst) {

            } else {
                this.sql().append(",");
            }
            this.merge(express);
        });
        return this;
    }


}
