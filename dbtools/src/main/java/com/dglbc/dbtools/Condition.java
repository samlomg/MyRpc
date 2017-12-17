package com.dglbc.dbtools;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Setter
@Getter
public class Condition implements Serializable{

    private String logic;
    private Conditions conditions;

}
