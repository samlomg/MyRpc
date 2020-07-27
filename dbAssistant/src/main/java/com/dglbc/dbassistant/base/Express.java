package com.dglbc.dbassistant.base;

import com.dglbc.dbassistant.tips.TipsShow;
import lombok.*;
import lombok.experimental.Accessors;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/29
 */

@Accessors(fluent = true)
@Setter
@Getter
@EqualsAndHashCode
public class Express {
    private StringBuilder sql = new StringBuilder();
    private List<Object> values = new ArrayList<>();


    private boolean sec = false;//是否是已经生成查询sql语句,默认是否

    public Express(String sql, List<Object> values) {
        this.sql.append(sql);
        this.values.addAll(values);

    }

    public Express(String sql) {
        this.sql.append(sql);
    }

    public Express(StringBuilder sql, List<Object> values, boolean sec) {
        this.sql.append(sql);
        this.values.addAll(values);
        this.sec = sec;
    }

    public Express(String sql, List<Object> values, boolean sec) {
        this.sql.append(sql);
        this.values.addAll(values);
        this.sec = sec;
    }

    public Express() {
    }

    //合并在一起
    public Express merge(Express express) {
        this.sql.append(express.sql());
        this.values.addAll(express.values());
        return this;
    }

    //合并在一起
    public <T> Express merge(List<T> expresss) {
        if (expresss != null && expresss.size() > 0) {
            if (expresss.get(0) instanceof Express) {
                for (T express : expresss) {
                    merge((Express) express);
                }
            } else if (expresss.get(0) instanceof SpecialExpress) {
                for (T express : expresss) {
                    merge((SpecialExpress) express);
                }
            }

        } else {
            TipsShow.alert("显示字段不能为空！");
        }
        return this;
    }

    //合并在一起
    public Express merge(String sql, List<Object> objects) {
        this.sql.append(sql);
        this.values.addAll(objects);
        return this;
    }

    public Express merge(Supplier<Express> s) {
        merge(s.get());
        return this;
    }

    public <T> Express merge(Function<T, Express> f, T s) {
        merge(f.apply(s));
        return this;
    }

    public <T> Express merge(Predicate<T> predicate, T t) {
        if (predicate.test(t)) {

        }
        return this;
    }

}
