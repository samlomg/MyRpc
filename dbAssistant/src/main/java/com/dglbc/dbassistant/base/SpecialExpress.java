package com.dglbc.dbassistant.base;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Accessors(fluent = true)
@Setter
@Getter
public class SpecialExpress extends Express {
    private String cateNate;

    public SpecialExpress(String sql, String cateNate) {
        super(sql);
        this.cateNate = cateNate;
    }

    public SpecialExpress(Express express) {
        this.merge(express);
    }

    public SpecialExpress(String cateNate,Express express) {
        this.merge(express);
        this.cateNate = cateNate;
    }

    public SpecialExpress(String sql) {
        super(sql);
    }

    public SpecialExpress(String sql, List<Object> values) {
        super(sql, values);
    }

    public SpecialExpress(String sql, List<Object> values, String cateNate) {
        super(sql, values);
        this.cateNate = cateNate;
    }

}
