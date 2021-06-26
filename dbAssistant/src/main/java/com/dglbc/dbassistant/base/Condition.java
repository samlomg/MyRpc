package com.dglbc.dbassistant.base;


import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.dml.Delete;
import com.dglbc.dbassistant.dml.Select;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.Arrays;

@Accessors(fluent = true)
@Setter
@Getter
@ToString
public abstract class Condition<T> extends Express {

    /**
     * 20200130希望where条件能单独一个功能抽取出来，这样潘多拉盒子系列就有下一步了！
     * 可以这么说用这个sql转换的人基本都对jdbc和db有一定的了解。
     * 所以我想where这个常用的动态的地方应该要做成可以灵活的。
     * 以便在任何地方的都有发挥余热的地方。
     */
    private Where wheres;

    public abstract T me();

    public void checkParts(AbstractExpress abstractExpress) {
        if (abstractExpress != null) {
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

    /**
     * where case
     */

    /**
     * commond
     */

    public T and(String where, Object... values) {
        where(where, values);
        return me();
    }

    public T or(String where, Object... values) {
        where(K.OR, where, values);
        return me();
    }

    public T where() {
        if (null == this.wheres()) {
            this.wheres = new Where();
        }
        return me();
    }

    public T where(String where, Object... values) {
        if (null == this.wheres()) {
            this.wheres = new Where();
        }
        this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where) : new SpecialExpress(where, Arrays.asList(values)));
        return me();
    }

    public T where(String cateNate, String where, Object... values) {
        if (cateNate == null || cateNate.trim() == "") {
            where(where, values);
        } else {

            if (null == this.wheres()) {
                this.wheres = new Where();
            }
            this.wheres.wheres().add(values.length == 0 ? new SpecialExpress(where, cateNate) : new SpecialExpress(where, Arrays.asList(values), cateNate));
        }
        return me();
    }


    public T where(String cateNate, String column, WK wk, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(WK.op(wk), value), value);
        return me();
    }

    public T where(String cateNate, String column, String wks, Object... value) {
        wheres().caulse(cateNate, column, WKUnit.getOperation(wks, value), value);
        return me();
    }


    public T where(String cateNate, Express express, WK wk, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(WK.op(wk), value), value);
        return me();
    }

    public T where(String cateNate, Express express, String wks, Object... value) {
        wheres().caulse(cateNate, express, WKUnit.getOperation(wks, value), value);
        return me();
    }

    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public T eq(String column, Object value) {
        where(K.AND, column, WK.EQ, value);
        return me();
    }

    /**
     * 等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T eq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.EQ, value);
        return me();
    }

    public T eq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.EQ, value);
        return me();
    }

    /**
     * >
     *
     * @param column
     * @param value
     * @return
     */
    public T gt(String column, Object value) {
        where(K.AND, column, WK.GT, value);
        return me();
    }

    /**
     * >
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T gt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.GT, value);
        return me();
    }

    public T gt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.GT, value);
        return me();
    }


    /**
     * <
     *
     * @param column
     * @param value
     * @return
     */
    public T lt(String column, Object value) {
        where(K.AND, column, WK.LT, value);
        return me();
    }

    /**
     * <
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T lt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LT, value);
        return me();
    }

    public T lt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LT, value);
        return me();
    }


    /**
     * >=
     *
     * @param column
     * @param value
     * @return
     */
    public T ge(String column, Object value) {
        where(K.AND, column, WK.op(WK.GT, WK.EQ), value);
        return me();
    }

    /**
     * >=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T ge(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.GT, WK.EQ), value);
        return me();
    }

    public T ge(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.GT, WK.EQ), value);
        return me();
    }


    /**
     * <=
     *
     * @param column
     * @param value
     * @return
     */
    public T le(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.EQ), value);
        return me();
    }

    /**
     * <=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T le(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.EQ), value);
        return me();
    }

    public T le(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.EQ), value);
        return me();
    }


    /**
     * between
     *
     * @param column
     * @param value
     * @return
     */
    public T between(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return me();
    }

    /**
     * between
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T between(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return me();
    }

    public T between(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);
        return me();
    }

    /**
     * in
     *
     * @param column
     * @param value
     * @return
     */
    public T in(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return me();
    }

    /**
     * in
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T in(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return me();
    }

    public T in(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.IN), value), value);
        return me();
    }


    /**
     * like
     *
     * @param column
     * @param value
     * @return
     */
    public T like(String column, Object value) {
        where(K.AND, column, WK.LIKE, value);
        return me();
    }

    /**
     * like
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T like(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LIKE, value);
        return me();
    }

    public T like(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LIKE, value);
        return me();
    }


    /**
     * isNull
     *
     * @param column
     * @param value
     * @return
     */
    public T isNull(String column, Object value) {
        where(K.AND, column, WK.op(WK.IS, WK.NULL), value);
        return me();
    }

    /**
     * isNull
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T isNull(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.IS, WK.NULL), value);
        return me();
    }

    public T isNull(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.IS, WK.NULL), value);
        return me();
    }


    /**
     * notBetween
     *
     * @param column
     * @param value
     * @return
     */
    public T notBetween(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return me();
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
    public T notBetween(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return me();
    }

    public T notBetween(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);
        return me();
    }

    /**
     * notIn
     *
     * @param column
     * @param value
     * @return
     */
    public T notIn(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return me();
    }

    /**
     * notIn
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T notIn(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return me();
    }

    public T notIn(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);
        return me();
    }


    /**
     * notLike
     *
     * @param column
     * @param value
     * @return
     */
    public T notLike(String column, Object value) {
        where(K.AND, column, WK.op(WK.NOT, WK.LIKE), value);
        return me();
    }

    /**
     * notLike
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T notLike(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NOT, WK.LIKE), value);
        return me();
    }

    public T notLike(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NOT, WK.LIKE), value);
        return me();
    }


    /**
     * isNotNull
     *
     * @param column
     * @return
     */
    public T isNotNull(String column) {
        where(K.AND, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return me();
    }

    /**
     * isNotNull
     *
     * @param cateNate
     * @param column
     * @return
     */
    public T isNotNull(String cateNate, String column) {
        where(cateNate, column, WK.op(WK.IS, WK.NOT, WK.NULL));
        return me();
    }

    public T isNotNull(String cateNate, Express express) {
        where(cateNate, express, WK.op(WK.IS, WK.NOT, WK.NULL));
        return me();
    }


    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public T neq(String column, Object value) {
        where(K.AND, column, WK.op(WK.NEQ), value);
        return me();
    }

    /**
     * 不等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public T neq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NEQ), value);
        return me();
    }

    public T neq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NEQ), value);
        return me();
    }


}
