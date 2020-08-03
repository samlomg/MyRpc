package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.declare.Response;
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

    @Override
    public Express toExpress() throws Exception {
        this.sql().append(K.WHERE);
        mergeWhere(wheres);
        this.sql().append(" ");
        return this;
    }

    @Override
    public Response isCheck() {
        Response re = new Response(200, "success");

        if (Unitls.isNull(wheres)){
            re.code(10001).status("Where check Fail!");
        }
        return re;
    }

    Express mergeWhere(List<SpecialExpress> wheres){
        wheres.forEach(specialExpress -> {
            this.sql().append(" ").append(specialExpress.cateNate() == null || specialExpress.cateNate().trim().equals("") ?
                    K.AND: specialExpress.cateNate());
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

    public Where caulse(String column, String operation, List values) {
        //根据情况 例如between 和in是要提前知道参数个数。所以先处理operation语句
        caulse(() -> {
            return new SpecialExpress(column + WKUnit.getOperation(operation, values), values);
        });
        return this;
    }

    public Where caulse(String column, String operation, Object... values) {
        return  caulse(column,operation, Arrays.asList(values));
    }
    
    //expression 作为参数有两种第一是查询条件 第二查询参数
    public Where caulse(Express express, String operation, List values) {
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
                return new SpecialExpress(express);
            });
        } else {
            caulse(() -> {
                express.merge(WKUnit.getOperation(operation, values), values);
                return new SpecialExpress(express);
            });
        }
        return this;
    }

    public Where caulse(Express express, String operation, Object... values) {
        return  caulse(express, operation, Arrays.asList(values));
    }

}
