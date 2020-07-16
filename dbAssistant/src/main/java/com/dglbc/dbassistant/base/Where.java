package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
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
public class Where extends Express implements AbstractExpress {

    private List<SpecialExpress> wheres = new ArrayList<>();

    public Where(SpecialExpress where) {
        wheres.add(where);
    }

    @Override
    public Express toExpress() throws Exception {
        this.sql().append(K.WHERE);
        this.merge(wheres);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(wheres)){
            re.code(10001).status("Where check Fail!");
        }
        return re;
    }

}
