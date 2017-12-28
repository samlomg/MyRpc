package com.dglbc.dbtools.join;

import com.dglbc.dbtools.Statement;
import com.dglbc.dbtools.SqlKey;
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
public class Join implements Serializable {

    private String join;//key world 1:left join,right join ...
    private String table; //
    private String alias;
    private List<JoinCondition> condition;
    private List<Object> parms;
    private String joinSql;

    public Statement builder(){
        StringBuilder sql = new StringBuilder().append(join).append(table).append(" ").append(alias).append(SqlKey.ON);
        boolean init=false;
        for (JoinCondition jionCondition:condition){
            if (init){
                sql.append(SqlKey.AND);
            }
            sql.append(jionCondition.builder());
            init = true;
        }
        return new Statement(sql.toString(),parms);
    }

    public Join(String join,String table, String alias,String aliasName, String value) {
        this.join = join;
        this.table = table;
        this.alias = alias;
        this.condition = new ArrayList<JoinCondition>(){{
            add(new JoinCondition(alias, aliasName,"?"));
        }};

        this.parms=new ArrayList<Object>(){{
            add(value);
        }};
    }

    public Join(String join,String table, String alias,String aliasName,String refences, String name) {
        this.join = join;
        this.table = table;
        this.alias = alias;
        this.condition = new ArrayList<JoinCondition>(){{
            add(new JoinCondition(refences, name, alias + "." + aliasName));
        }};
        this.parms = new ArrayList<>();
    }

    public Join(String join, Statement table, String alias, String aliasName, String refences, String name) {
        this.join = join;
        this.table = " ( "+table.getSql()+" ) ";
        this.alias = alias;
        this.condition = new ArrayList<JoinCondition>(){{
            add(new JoinCondition(refences, name, alias + "." + aliasName));
        }};
        this.parms = new ArrayList<>();
        if (table.getValues().size() >0) this.parms.addAll(table.getValues());

    }

    public Join on(String alias,String aliasName, String name){
        this.condition.add(new JoinCondition("A", name, alias + "." + aliasName));
        return this;
    }

    public Join onA(String alias,String aliasName,String refences, String name){
        this.condition.add(new JoinCondition(refences, name, alias + "." + aliasName));
        return this;
    }

    public Join on(String clause,String name){
        this.condition.add(new JoinCondition("A", name, clause));
        return this;
    }

    public Join onA(String clause,String refences, String name){
        this.condition.add(new JoinCondition(refences, name, clause));
        return this;
    }


}
