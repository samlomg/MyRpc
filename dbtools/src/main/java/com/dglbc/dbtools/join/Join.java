package com.dglbc.dbtools.join;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Accessors(chain = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public abstract class Join implements Serializable {

    private String table;
    private String alias;
    private List<JoinCondition> condition;

    public Join(String table, String alias,String aliasName, String name) {
        this.table = table;
        this.alias = alias;
        this.condition = new ArrayList<JoinCondition>(){{
            new JoinCondition("A", name, alias + "." + aliasName);
        }};
    }

    public Join(String table, String alias,String aliasName,String refences, String name) {
        this.table = table;
        this.alias = alias;
        this.condition = new ArrayList<JoinCondition>(){{
            new JoinCondition(refences, name, alias + "." + aliasName);
        }};
    }

    public Join addCondition(String alias,String aliasName, String name){
        this.condition.add(new JoinCondition("A", name, alias + "." + aliasName));
        return this;
    }

    public Join addCondition(String alias,String aliasName,String refences, String name){
        this.condition.add(new JoinCondition(refences, name, alias + "." + aliasName));
        return this;
    }

    public Join addConditionClause(String clause,String name){
        this.condition.add(new JoinCondition("A", name, clause));
        return this;
    }

    public Join addConditionClause(String clause,String refences, String name){
        this.condition.add(new JoinCondition(refences, name, clause));
        return this;
    }


}
