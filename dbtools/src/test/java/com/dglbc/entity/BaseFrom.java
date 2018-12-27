package com.dglbc.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BaseFrom {
    //@Excel(name = "类型")
    private int attrType;
    //@Excel(name = "java属性名")
    private String attrName;
    //@Excel(name = "字段名")
    private String sqlAttrName;
    //@Excel(name = "说明")
    private String rmk;

    public BaseFrom(int attrType, String attrName) {
        this.attrType = attrType;
        this.attrName = attrName;
    }
}
