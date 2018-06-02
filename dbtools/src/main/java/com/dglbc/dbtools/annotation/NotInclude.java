package com.dglbc.dbtools.annotation;

import java.lang.annotation.*;

/**
 * insert,update
 * 自动生成不要这个字段的
 */

@Documented
@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE, ElementType.FIELD})//次注解作用于类和字段上
public @interface NotInclude {
}
