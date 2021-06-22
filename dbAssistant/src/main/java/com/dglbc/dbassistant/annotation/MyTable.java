package com.dglbc.dbassistant.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface MyTable {
    String tableName() default "";

    String alias() default "";
}
