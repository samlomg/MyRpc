package com.dglbc.dbassistant.base;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Created by LBC on 2018/1/11
 **/

@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProduceParameter {
    private int num;
    private ParameterMode mode;
    private Object value;

    public ProduceParameter(ParameterMode mode, Object value) {
        this.mode = mode;
        this.value = value;
    }
}
