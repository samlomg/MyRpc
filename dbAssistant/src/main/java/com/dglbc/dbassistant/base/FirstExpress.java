package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(fluent = true)
@Setter
@Getter
@EqualsAndHashCode
public class FirstExpress extends Express implements AbstractExpress {

    private Express first;

    public FirstExpress(String sql) {
        this.first = new Express(sql);
    }

    public FirstExpress() {
    }

    public void af(String sql) {
        if (null == first) {
            first = new Express(sql);
        } else {
            first.sql().append(" ").append(sql).append(" ");
        }
    }

    public void af(Express sql) {
        if (null == first) {
            first = sql;
        } else {
            first.merge(sql);
        }
    }

    @Override
    public Express toExpress() throws Exception {
        return this.merge(first);
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(first)) {
            re.code(10001).status("First check Fail!");
        }
        return re;
    }
}
