package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.unitils.Unitls;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/31 22:52
 * <p>
 * left join tableB alias on wheres
 */
@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ExpressWithTable extends Express implements AbstractExpress {

    private String join;
    private Table table;
    private List<SpecialExpress> ons;

    @Override
    public Express toExpress() throws Exception {
        if (null == join) {
            this.sql().append(K.LEFTJOIN);
        } else {
            //暂时这样，以后会修
            this.sql().append(join);
        }
        this.merge(table.toExpress());
        this.sql().append(K.ON);
        this.merge(ons);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(table) || Unitls.isNull(ons)){
            re.code(10001).status("ExpressWithTable check Fail!");
        }
        return re;
    }


}
