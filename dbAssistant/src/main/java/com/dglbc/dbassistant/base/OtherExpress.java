package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Setter
@Getter
@EqualsAndHashCode
public class OtherExpress extends Express implements AbstractExpress {

    private List<Express> others = new ArrayList<>();

    @Override
    public Express toExpress() throws Exception {
        if (sec()) clear();
        this.merge(others);
        this.sql().append(" ");
        this.sec(true);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(others)){
            re.code(10001).status("Others check Fail!");
        }
        return re;
    }



}
