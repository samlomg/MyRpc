package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
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

    public static Column create() {
        return new Column();
    }

    /**
     * 这是update 专用
     * @param column
     * @param value
     * @return
     */
    public Column set(String column, Object... value) {
        if (column == null || column.trim().length() == 0) return this;
        columns.add(new Express(column + " = ? ", value == null || value.length == 0 ? null : Arrays.asList(value)));
        return this;
    }

    /**
     * 这是update 专用
     * @param column
     * @return
     */
    public Column set(Column column) {
        if (column == null) return this;
        columns.addAll(column.columns());
        return this;
    }

    @Override
    public Express toExpress() throws Exception {
        if (sec()) clear();
        if (columns == null || columns.size() == 0) {
            TipsShow.alert("SQL查询语句必须有查询的项");
        }

        mergeColumns(columns);
        this.sec(true);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(columns)) {
            re.code(10001).status("Column check Fail!");
        }
        return re;
    }

    Express mergeColumns(List<Express> columns) {
        boolean isFirst = true;
        for (Express express : columns) {
            if (isFirst) {
                isFirst = false;
            } else {
                this.sql().append(",");
            }
            this.merge(express);
        }
        return this;
    }


}
