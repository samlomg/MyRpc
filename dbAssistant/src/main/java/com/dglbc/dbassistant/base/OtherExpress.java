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
public class OtherExpress extends Express implements AbstractExpress {

    private Express other;

    @Override
    public Express toExpress() throws Exception {
        return this.merge(other);
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(other)){
            re.code(10001).status("Other check Fail!");
        }
        return re;
    }
}
