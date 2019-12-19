package com.dglbc.dbtools.where;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Getter
@AllArgsConstructor
public enum  WK {
    IN("IN"," IN ( %s )"),
    BETWEEN("BETWEEN"," BETWEEN %s AND %s"),
    NULL("NULL"," NULL "),
    IS("IS"," IS %s "),
    EQ("="," = %s "),
    LT("<"," < %s "),
    GT(">"," > %s "),
    NOT("NOT"," NOT %s "),
    LIKE("LIKE"," LIKE %s ");

    private String desc;
    private String format;
}
