package com.dglbc.dbassistant.annotation;

import java.lang.annotation.*;

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD, ElementType.METHOD})
public @interface MyColumn {
    String columnName() default "";

    String as() default "";
}
