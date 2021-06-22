package com.dglbc.sample;

import com.dglbc.annotation.MyTable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;

public class AnnotationApp {
    public static void main(String[] args) {
        Class<T0027> clz = T0027.class;
        // 判断类上是否有次注解
        boolean clzHasAnno = clz.isAnnotationPresent(MyTable.class);
        System.out.println(clzHasAnno);
        if (clzHasAnno) {
            // 获取类上的注解
            MyTable annotation = clz.getAnnotation(MyTable.class);
            // 输出注解上的属性
            System.out.println("注解" + annotation.name());

        }

        Field[] fields = clz.getDeclaredFields();//只能获取该类，如果想要获取父类的只能
        for (Field field : fields) {
            System.out.println(field.getName() + ":" + field.isAnnotationPresent(MyTable.class));
        }

        //解析方法上的注解
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            boolean methodHasAnno = method.isAnnotationPresent(MyTable.class);
            if (methodHasAnno) {
                //得到注解
                MyTable methodAnno = method.getAnnotation(MyTable.class);
                //输出注解属性
                String desc = methodAnno.desc();
                System.out.println(method.getName() + " desc = " + desc);
            }
        }
    }

    public void test1(Class<T0027> clz){
        boolean clzHasAnno = clz.isAnnotationPresent(MyTable.class);
        System.out.println(clzHasAnno);
        if (clzHasAnno) {
            // 获取类上的注解
            MyTable annotation = clz.getAnnotation(MyTable.class);
            // 输出注解上的属性
            System.out.println("注解" + annotation.name());

        }
    }
}
