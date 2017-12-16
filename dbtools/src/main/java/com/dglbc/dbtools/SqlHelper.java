package com.dglbc.dbtools;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;

@Accessors(chain = true)
@Setter
@Getter
public class SqlHelper implements Serializable{
    private int mode;
    private String selectContent;
    private String insertContent;
    private String insertValues;
    private String updateContent;
    private String where;
    private String table;
    private String joinString;

    private Object[] params;

    

}
