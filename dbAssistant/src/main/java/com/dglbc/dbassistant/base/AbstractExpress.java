package com.dglbc.dbassistant.base;


import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/29
 */

public interface AbstractExpress {
    public Express toExpress() throws Exception;
}
