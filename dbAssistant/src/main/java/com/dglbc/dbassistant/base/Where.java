package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
import com.dglbc.dbassistant.dml.Select;
import com.dglbc.dbassistant.in.WK;
import com.dglbc.dbassistant.tips.TipsShow;
import com.dglbc.dbassistant.unitils.Unitls;
import com.dglbc.dbassistant.unitils.WKUnit;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/31 22:58
 */
@Accessors(fluent = true)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Where extends Express implements AbstractExpress {

    private List<SpecialExpress> wheres = new ArrayList<>();

    public Where(SpecialExpress where) {
        wheres.add(where);
    }

    public static Where me(){
        return new Where();
    }

    @Override
    public Express toExpress() throws Exception {
        if (sec()) clear();
        this.sql().append(K.WHERE);
        mergeWhere(wheres);
        this.sql().append(" ");
        this.sec(true);
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(wheres)) {
            re.code(10001).status("Where check Fail!");
        }
        return re;
    }

    Express mergeWhere(List<SpecialExpress> wheres) {
        wheres.forEach(specialExpress -> {
            this.sql().append(" ").append(specialExpress.cateNate() == null || specialExpress.cateNate().trim().equals("") ?
                    K.AND : specialExpress.cateNate());
            this.sql().append(" ").append(specialExpress.sql());
            this.values().addAll(specialExpress.values());
        });
        return this;
    }

    //用java8的特性
    public Where caulse(Supplier<SpecialExpress> s) {
        caulse(s.get());
        return this;
    }

    // 运算
    public Where caulse(SpecialExpress express) {
        wheres.add(express);
        return this;
    }

    public Where caulse(String cateNate, String column, String operation, List values) {
        //根据情况 例如between 和in是要提前知道参数个数。所以先处理operation语句
        caulse(() -> {
            return new SpecialExpress(column + WKUnit.getOperation(operation, values), values, cateNate);
        });
        return this;
    }

    public Where caulse(String cateNate, String column, String operation, Object... values) {
        return caulse(cateNate, column, operation, Arrays.asList(values));
    }

    public Where caulse( String column, String operation, Object... values) {
        return caulse(K.AND, column, operation, Arrays.asList(values));
    }

    //expression 作为参数有两种第一是查询条件 第二查询参数
    public Where caulse(String cateNate, Express express, String operation, List values) {
        if (values != null && values.size() > 0 && values.get(0) instanceof Express) {
            caulse(() -> {
                StringBuilder op = new StringBuilder();
                List parms = new ArrayList();
                if (operation.toUpperCase().indexOf("BETWEEN") > -1 && values.size() == 2) {
                    //between
                    op.append(String.format(operation,
                            K.LEFT + ((Express) values.get(0)).sql().toString() + K.RIGHT,
                            K.LEFT + ((Express) values.get(1)).sql().toString() + K.RIGHT));
                    parms.addAll(((Express) values.get(0)).values());
                    parms.addAll(((Express) values.get(1)).values());
                } else if (operation.toUpperCase().indexOf("IN") > -1) {
                    parms.addAll(((Express) values.get(0)).values());
                    op.append(String.format(operation, K.LEFT + ((Express) values.get(0)).sql().toString() + K.RIGHT));
                } else if (operation.toUpperCase().indexOf("NULL") > -1) {
                    op.append(operation);
                } else if (values.size() == 1) {
                    parms.addAll(((Express) values.get(0)).values());
                    op.append(String.format(operation, K.LEFT + ((Express) values.get(0)).sql().toString() + K.RIGHT));
                } else {
                    TipsShow.alert("操作符号请按标准的输入");
                }
                express.merge(K.LEFT + op + K.RIGHT, values);
                return new SpecialExpress(cateNate, express);
            });
        } else {
            caulse(() -> {
                express.merge(WKUnit.getOperation(operation, values), values);
                return new SpecialExpress(cateNate, express);
            });
        }
        return this;
    }

    public Where caulse(Express express, String operation, Object... values) {
        return caulse(K.AND, express, operation, Arrays.asList(values));
    }

    public Where caulse(String cateNate, Express express, String operation, Object... values) {
        return caulse(cateNate, express, operation, Arrays.asList(values));
    }

    public void where(String cateNate, String column, WK wk, Object... value) {
        caulse(cateNate, column, WKUnit.getOperation(WK.op(wk), value), value);
    }

    public void where(String cateNate, String column, String wks, Object... value) {
        caulse(cateNate, column, WKUnit.getOperation(wks, value), value);
    }


    public void where(String cateNate, Express express, WK wk, Object... value) {
        caulse(cateNate, express, WKUnit.getOperation(WK.op(wk), value), value);
    }

    public void where(String cateNate, Express express, String wks, Object... value) {
        caulse(cateNate, express, WKUnit.getOperation(wks, value), value);
    }



    /**
     * 等于
     *
     * @param column
     * @param value
     * @return
     */
    public void eq(String column, Object value) {
        where(K.AND, column, WK.EQ, value);

    }

    /**
     * 等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void eq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.EQ, value);

    }

    public void eq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.EQ, value);

    }

    /**
     * >
     *
     * @param column
     * @param value
     * @return
     */
    public void gt(String column, Object value) {
        where(K.AND, column, WK.GT, value);

    }

    /**
     * >
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void gt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.GT, value);

    }

    public void gt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.GT, value);

    }


    /**
     * <
     *
     * @param column
     * @param value
     * @return
     */
    public void lt(String column, Object value) {
        where(K.AND, column, WK.LT, value);

    }

    /**
     * <
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void lt(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LT, value);

    }

    public void lt(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LT, value);

    }


    /**
     * >=
     *
     * @param column
     * @param value
     * @return
     */
    public void ge(String column, Object value) {
        where(K.AND, column, WK.op(WK.GT, WK.EQ), value);

    }

    /**
     * >=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void ge(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.GT, WK.EQ), value);

    }

    public void ge(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.GT, WK.EQ), value);

    }


    /**
     * <=
     *
     * @param column
     * @param value
     * @return
     */
    public void le(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.EQ), value);

    }

    /**
     * <=
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void le(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.EQ), value);

    }

    public void le(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.EQ), value);

    }


    /**
     * between
     *
     * @param column
     * @param value
     * @return
     */
    public void between(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);

    }

    /**
     * between
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void between(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);

    }

    public void between(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.BETWEEN.getFormat(), value, value2), value, value2);

    }

    /**
     * in
     *
     * @param column
     * @param value
     * @return
     */
    public void in(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.IN), value), value);

    }

    /**
     * in
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void in(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.IN), value), value);

    }

    public void in(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.IN), value), value);

    }


    /**
     * like
     *
     * @param column
     * @param value
     * @return
     */
    public void like(String column, Object value) {
        where(K.AND, column, WK.LIKE, value);

    }

    /**
     * like
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void like(String cateNate, String column, Object value) {
        where(cateNate, column, WK.LIKE, value);

    }

    public void like(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.LIKE, value);

    }


    /**
     * isNull
     *
     * @param column
     * @param value
     * @return
     */
    public void isNull(String column, Object value) {
        where(K.AND, column, WK.op(WK.IS, WK.NULL), value);

    }

    /**
     * isNull
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void isNull(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.IS, WK.NULL), value);

    }

    public void isNull(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.IS, WK.NULL), value);

    }


    /**
     * notBetween
     *
     * @param column
     * @param value
     * @return
     */
    public void notBetween(String column, Object value, Object value2) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);

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
    public void notBetween(String cateNate, String column, Object value, Object value2) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);

    }

    public void notBetween(String cateNate, Express express, Object value, Object value2) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.BETWEEN), value, value2), value, value2);

    }

    /**
     * notIn
     *
     * @param column
     * @param value
     * @return
     */
    public void notIn(String column, Object... value) {
        where(K.AND, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);

    }

    /**
     * notIn
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void notIn(String cateNate, String column, Object... value) {
        where(cateNate, column, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);

    }

    public void notIn(String cateNate, Express express, Object... value) {
        where(cateNate, express, WKUnit.getOperation(WK.op(WK.NOT, WK.IN), value), value);

    }


    /**
     * notLike
     *
     * @param column
     * @param value
     * @return
     */
    public void notLike(String column, Object value) {
        where(K.AND, column, WK.op(WK.NOT, WK.LIKE), value);

    }

    /**
     * notLike
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void notLike(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.NOT, WK.LIKE), value);

    }

    public void notLike(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.NOT, WK.LIKE), value);

    }


    /**
     * isNotNull
     *
     * @param column
     * @return
     */
    public void isNotNull(String column) {
        where(K.AND, column, WK.op(WK.IS, WK.NOT, WK.NULL));

    }

    /**
     * isNotNull
     *
     * @param cateNate
     * @param column
     * @return
     */
    public void isNotNull(String cateNate, String column) {
        where(cateNate, column, WK.op(WK.IS, WK.NOT, WK.NULL));

    }

    public void isNotNull(String cateNate, Express express) {
        where(cateNate, express, WK.op(WK.IS, WK.NOT, WK.NULL));

    }


    /**
     * 不等于
     *
     * @param column
     * @param value
     * @return
     */
    public void neq(String column, Object value) {
        where(K.AND, column, WK.op(WK.LT, WK.GT), value);

    }

    /**
     * 不等于
     *
     * @param cateNate
     * @param column
     * @param value
     * @return
     */
    public void neq(String cateNate, String column, Object value) {
        where(cateNate, column, WK.op(WK.LT, WK.GT), value);

    }

    public void neq(String cateNate, Express express, Object value) {
        where(cateNate, express, WK.op(WK.LT, WK.GT), value);

    }


}
