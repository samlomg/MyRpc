package com.dglbc.dbtools.assistant;


import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.table.Column;
import com.dglbc.dbtools.table.Table;
import com.dglbc.dbtools.where.Where;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/*
    退一万步来说，直接生成就好，不需要残留，不需要这么复杂的生成过程。
    不是要面面俱到，而是要辅助写sql语句和看起来不这么乱。不会检查语法，不会智能的干什么。单纯就是指哪打哪。（理念清晰，之前的想法先放一放）
 */
@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
public class Select {
    private List<String> content = new ArrayList<>();//查询的名称 外联的
    private String special = null;
    private List<String> joins = null;
    private String groupContent = null;
    private String orderContent = null;
    private String havingConditions = null;

    public Select sc(String column) {
        this.content.add(column);
        return this;
    }

    public Select sp(String sp) {
        this.special = sp;
        return this;
    }

    public Select join(String join) {
        this.joins.add(join);
        return this;
    }

    public Select group(String group){
        this.groupContent = group;
        return this;
    }

    public Select order(String order){
        this.orderContent = order;
        return this;
    }

    public Select having(String having){
        this.havingConditions = having;
        return this;
    }

    //接下来要处理查询条件
    public Select op(String sql,String operation, List values){
        return this;
    }




}
