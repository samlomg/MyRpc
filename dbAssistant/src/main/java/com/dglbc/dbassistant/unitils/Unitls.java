package com.dglbc.dbassistant.unitils;

import java.util.List;

/**
 * @version 1.0
 * @Author LBC
 * @date 2020/1/30 15:23
 */
public class Unitls {
    public static boolean isNull(String source) {

        return source.isEmpty();
    }

    public static boolean isNull(List source){
        return source == null || source.size() == 0;
    }

    public static <T> boolean isNull(T source){
        return source == null;
    }

}
