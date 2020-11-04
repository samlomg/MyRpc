package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;

/**
 * 20201014决定只做单表更新
 * update A set ab=? where id =1
 */
@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
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
        if (sec()) clear();
        this.sql().append(K.UPDATE).append(table);
        if (isNormal) this.sql().append(K.SET);
        checkParts(columns);
        checkParts(wheres);
        //重要一点如果是已经构建生成的express必须有标志
        this.sec(true);
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

    public Update set(Express column) {
        if (this.columns == null) this.columns = new Column();
        this.isNormal = true;
        this.columns.columns().add(column);
        return this;
    }


    public Update set(String column, Object... value) {
        if (this.columns == null) this.columns = new Column();
        this.isNormal = true;
        this.columns.columns().add(new Express(column + " = ? ", value == null || value.length == 0 ? null : Arrays.asList(value)));
        return this;
    }

    public static Update TABLE(String table) {
        return new Update(table);
    }


    public Update where(String where, Object... values) {
        if (null == this.wheres()) {
            this.wheres = new Where();
        }
        this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where) : new SpecialExpress(where, Arrays.asList(values)));
        return this;
    }

    public Update where(String cateNate, String where, Object... values) {
        if (cateNate == null || cateNate.trim() == "") {
            where(where, values);
        } else {

            if (null == this.wheres()) {
                this.wheres = new Where();
            }
            this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where, cateNate) : new SpecialExpress(where, Arrays.asList(values), cateNate));
        }
        return this;
    }

    public Update and(String where, Object... values) {
        where(where, values);
        return this;
    }

    public Update or(String where, Object... values) {
        where(K.OR, where, values);
        return this;
    }


    /**
     * where case
     */

    /**
     * commond
     */

    public Update where(String cateNate, String column, WK wk, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Update where(String cateNate, String column, String wks, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(wks, value), value);
        return this;
    }


    public Update where(String cateNate, Express express, WK wk, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Update where(String cateNate, Express express, String wks, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(wks, value), value);
        return this;
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public Update eq(String column, Object value) {
        where(K.AND, column, WK.EQ, value);
        return this;
    }

    /**
     * 等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update eq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.EQ, value);
        return this;
    }

    public Update eq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.EQ, value);
        return this;
    }

    /**
     * >
     *
     * @param column
     * @param value
     * @return
     */
    public Update gt(String column, Object value) {
        where(K.AND, column, WK.GT, value);
        return this;
    }

    /**
     * >
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update gt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.GT, value);
        return this;
    }

    public Update gt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.GT, value);
        return this;
    }


    /**
     * <
     *
     * @param column
     * @param value
     * @return
     */
    public Update lt(String column, Object value) {
        where(K.AND, column, WK.LT, value);
        return this;
    }

    /**
     * <
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update lt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LT, value);
        return this;
    }

    public Update lt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LT, value);
        return this;
    }


    /**
     * >=
     *
     * @param column
     * @param value
     * @return
     */
    public Update ge(String column, Object value) {
        where(K.AND, column, WK.op(WK.GT, WK.EQ), value);
        return this;
    }

    /**
     * >=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update ge(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.GT, WK.EQ), value);
        return this;
    }

    public Update ge(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.GT, WK.EQ), value);
        return this;
    }


    /**
     * <=
     *
     * @param column
     * @param value
     * @return
     */
    public Update le(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.EQ), value);
        return this;
    }

    /**
     * <=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update le(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.EQ), value);
        return this;
    }

    public Update le(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.EQ), value);
        return this;
    }


    /**
     * between
     *
     * @param column
     * @param value
     * @return
     */
    public Update between(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    /**
     * between
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update between(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    public Update between(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    /**
     * in
     *
     * @param column
     * @param value
     * @return
     */
    public Update in(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }

    /**
     * in
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update in(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }

    public Update in(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }


    /**
     * like
     *
     * @param column
     * @param value
     * @return
     */
    public Update like(String column, Object value) {
        where(K.AND, column, WK.LIKE, value);
        return this;
    }

    /**
     * like
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update like(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LIKE, value);
        return this;
    }

    public Update like(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LIKE, value);
        return this;
    }


    /**
     * isNull
     *
     * @param column
     * @param value
     * @return
     */
    public Update isNull(String column, Object value) {
        where(K.AND, column, WK.op(WK.IS, WK.NULL), value);
        return this;
    }

    /**
     * isNull
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update isNull(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.IS, WK.NULL), value);
        return this;
    }

    public Update isNull(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.IS, WK.NULL), value);
        return this;
    }


    /**
     * notBetween
     *
     * @param column
     * @param value
     * @return
     */
    public Update notBetween(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    /**
     * between
     * notBetween
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update notBetween(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    public Update notBetween(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    /**
     * notIn
     *
     * @param column
     * @param value
     * @return
     */
    public Update notIn(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }

    /**
     * notIn
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update notIn(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }

    public Update notIn(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }


    /**
     * notLike
     *
     * @param column
     * @param value
     * @return
     */
    public Update notLike(String column, Object value) {
        where(K.AND, column, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }

    /**
     * notLike
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update notLike(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }

    public Update notLike(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }


    /**
     * isNotNull
     *
     * @param column
     * @return
     */
    public Update isNotNull(String column) {
        where(K.AND, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }

    /**
     * isNotNull
     *
     * @param cateNate
     * @param column
     * @return
     */
    public Update isNotNull(String cateNate, String column) {
        where(cateNate, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }

    public Update isNotNull(String cateNate, Express express) {
        where(cateNate, express, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }


    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public Update neq(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.GT), value);
        return this;
    }

    /**
     * 不等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public Update neq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.GT), value);
        return this;
    }

    public Update neq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.GT), value);
        return this;
    }

}
