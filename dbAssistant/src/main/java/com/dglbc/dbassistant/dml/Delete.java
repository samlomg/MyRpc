package com.dglbc.dbassistant.dml;

import com.dglbc.dbassistant.base.*;
import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;


/*
 * 目前只作单表更新的操作
 *delete from table where a=2
 * */

@Accessors(fluent = true)
@Setter
@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Delete extends Express {

    private String table;//主表有查询语句必有主表查询的


    /**
     *
     */
    private Where wheres;

    public Express build() {
        //build
        if (sec()) clear();
        this.sql().append(K.DELETE).append(K.FROM).append(table);
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

    public static Delete from(String table) {
        return new Delete(table);
    }

    public Delete(String table) {
        this.table = table;
    }


    public Delete where(String where, Object... values) {
        if (null == this.wheres()) {
            this.wheres = new Where();
        }
        this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where) : new SpecialExpress(where, Arrays.asList(values)));
        return this;
    }

    public Delete where(String cateNate, String where, Object... values) {
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

    public Delete and(String where, Object... values) {
        where(where, values);
        return this;
    }

    public Delete or(String where, Object... values) {
        where(K.OR, where, values);
        return this;
    }


    /**
     * where case
     */

    /**
     * commond
     */

    public Delete where(String cateNate, String column, WK wk, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Delete where(String cateNate, String column, String wks, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(wks, value), value);
        return this;
    }


    public Delete where(String cateNate, Express express, WK wk, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(WK.op(wk), value), value);
        return this;
    }

    public Delete where(String cateNate, Express express, String wks, Object... value) {
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
    public Delete eq(String column, Object value) {
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
    public Delete eq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.EQ, value);
        return this;
    }

    public Delete eq(String cateNate, Express express, Object value) {
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
    public Delete gt(String column, Object value) {
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
    public Delete gt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.GT, value);
        return this;
    }

    public Delete gt(String cateNate, Express express, Object value) {
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
    public Delete lt(String column, Object value) {
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
    public Delete lt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LT, value);
        return this;
    }

    public Delete lt(String cateNate, Express express, Object value) {
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
    public Delete ge(String column, Object value) {
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
    public Delete ge(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.GT, WK.EQ), value);
        return this;
    }

    public Delete ge(String cateNate, Express express, Object value) {
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
    public Delete le(String column, Object value) {
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
    public Delete le(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.EQ), value);
        return this;
    }

    public Delete le(String cateNate, Express express, Object value) {
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
    public Delete between(String column, Object value, Object value2) {
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
    public Delete between(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return this;
    }

    public Delete between(String cateNate, Express express, Object value, Object value2) {
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
    public Delete in(String column, Object... value) {
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
    public Delete in(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return this;
    }

    public Delete in(String cateNate, Express express, Object... value) {
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
    public Delete like(String column, Object value) {
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
    public Delete like(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LIKE, value);
        return this;
    }

    public Delete like(String cateNate, Express express, Object value) {
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
    public Delete isNull(String column, Object value) {
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
    public Delete isNull(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.IS, WK.NULL), value);
        return this;
    }

    public Delete isNull(String cateNate, Express express, Object value) {
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
    public Delete notBetween(String column, Object value, Object value2) {
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
    public Delete notBetween(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return this;
    }

    public Delete notBetween(String cateNate, Express express, Object value, Object value2) {
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
    public Delete notIn(String column, Object... value) {
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
    public Delete notIn(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return this;
    }

    public Delete notIn(String cateNate, Express express, Object... value) {
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
    public Delete notLike(String column, Object value) {
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
    public Delete notLike(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }

    public Delete notLike(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NOT, WK.LIKE), value);
        return this;
    }


    /**
     * isNotNull
     *
     * @param column
     * @return
     */
    public Delete isNotNull(String column) {
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
    public Delete isNotNull(String cateNate, String column) {
        where(cateNate, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return this;
    }

    public Delete isNotNull(String cateNate, Express express) {
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
    public Delete neq(String column, Object value) {
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
    public Delete neq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.GT), value);
        return this;
    }

    public Delete neq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.GT), value);
        return this;
    }

}
