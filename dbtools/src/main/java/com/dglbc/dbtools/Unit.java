package com.dglbc.dbtools;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Setter
@Getter
public class Unit implements Serializable {
    private String name;
    private Object value;

    public Unit(String name, Object value) {
        this.name = name;
        this.value = value;
    }
}
