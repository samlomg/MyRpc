package com.dglbc.dbtools.produce;

import lombok.*;
import lombok.experimental.Accessors;

/**
 * Created by LBC on 2018/1/11
 **/

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ProduceParameter {
    private int num;
    private ParameterMode mode;
    private Object value;
}
