package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.Express;
import com.dglbc.dbassistant.base.K;
import com.dglbc.dbassistant.base.ParameterMode;
import com.dglbc.dbassistant.base.ProduceParameter;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;

@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Call extends Express {
    private List<ProduceParameter> produceParameters = new ArrayList<>();
    private String produceFuntion;

    //存储过程
    public Call call(String sql) {
        this.produceFuntion = sql;
        this.produceParameters = new ArrayList<>();
        return this;
    }

    public Call cc(ParameterMode parameterMode, int num, Object parm) {
        produceParameters.add(new ProduceParameter(num, parameterMode, parm));
        return this;
    }

    public Call cc(int num, Object parm) {
        produceParameters.add(new ProduceParameter(num, ParameterMode.IN, parm));
        return this;
    }

    public Call cc(Object parm) {
        produceParameters.add(new ProduceParameter(ParameterMode.IN, parm));
        return this;
    }

    public Express build(){
        this.sql().append(" { ").append(produceFuntion).append(K.LEFT);
        StringBuilder tem1 = new StringBuilder();
        for (ProduceParameter produceParameter : produceParameters) {
            tem1.append(",?");
            this.values().add(produceParameter);
        }
        this.sql().append(tem1.delete(0, 1)).append(K.RIGHT).append(" } ");
        return this;
    }

}
