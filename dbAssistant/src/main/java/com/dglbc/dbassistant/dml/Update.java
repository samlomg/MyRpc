package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.tips.TipsShow;

import java.util.List;

/**
 * 20201014决定只做单表更新
 * update A set ab=? where id =1
 */
public class Update extends Express {

    private boolean isNormal = false;

    /**
     * 100%的查询语句是带查询的列名表名
     * 基础都用express会提供转化的方法
     */
    private Column columns;

    private String table;//主表有查询语句必有主表查询的


    /**
     * 20200130希望where条件能单独一个功能抽取出来，这样潘多拉盒子系列就有下一步了！
     * 可以这么说用这个sql转换的人基本都对jdbc和db有一定的了解。
     * 所以我想where这个常用的动态的地方应该要做成可以灵活的。
     * 以便在任何地方的都有发挥余热的地方。
     */
    private Where wheres;


    public Express build() {
        //初始化

        //build
        if (!sec()) {
            this.sql().append(K.UPDATE).append(table);
            if (isNormal) this.sql().append(K.SET);
            checkParts(columns);
            checkParts(wheres);
            //重要一点如果是已经构建生成的express必须有标志
            this.sec(true);
        }
        return this;
    }

    private void checkParts(AbstractExpress abstractExpress) {
        if (abstractExpress == null) {

        } else {
            Response response = abstractExpress.isCheck();
            if (response.code() == 200 || response.code() == 10001) {
                try {
                    this.merge(abstractExpress.toExpress());
                } catch (Exception e) {
                    e.printStackTrace();
                    TipsShow.alert("merge fail");
                }
            }
        }

    }

    //先来最多的情况
    public Update(String table, String col, String where) {
        this.isNormal = true;
        this.columns = new Column(new Express(col));
        this.table = table;
        //todo 目前没想到什么好的办法只能先new然后赋值
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres = new Where(specialExpress);
    }

    //补充情况
    public Update(String table, String where) {
        this.table = table;
        SpecialExpress specialExpress = new SpecialExpress(where);
        this.wheres = new Where(specialExpress);
    }

    public Update(String sqlAll) {
        this.table = sqlAll;
    }

    public Update set() {
        this.isNormal = true;
        return this;
    }

    public static Update TABLE(String table){
        return new Update(table);
    }
}
