package com.dglbc.data.jdbc;

public class ReflectUntil {
    public static Class<?> wrapper(Class<?> type) {
        if (type == null) {
            return null;
        } else if (type.isPrimitive()) {
            if (boolean.class == type) {
                return Boolean.class;
            } else if (int.class == type) {
                return Integer.class;
            } else if (long.class == type) {
                return Long.class;
            } else if (short.class == type) {
                return Short.class;
            } else if (byte.class == type) {
                return Byte.class;
            } else if (double.class == type) {
                return Double.class;
            } else if (float.class == type) {
                return Float.class;
            } else if (char.class == type) {
                return Character.class;
            } else if (void.class == type) {
                return Void.class;
            }
        }
        return type;
    }

    public static boolean isJavaClass(Class<?> clz) {
        return clz != null && clz.getClassLoader() == null;
    }

//    public static String wrapper(Class<?> type) {
//        if (type.isPrimitive()) {
//            if (boolean.class == type) {
//                return "Boolean";
//            } else if (int.class == type) {
//                return "Int";
//            } else if (long.class == type) {
//                return "Long";
//            } else if (short.class == type) {
//                return "Short";
//            } else if (byte.class == type) {
//                return "Byte";
//            } else if (double.class == type) {
//                return "Double";
//            } else if (float.class == type) {
//                return "Float";
//            } else if (char.class == type) {
//                return "Character";
//            }
//        }else {
//            if (Integer.class == type) {
//                return int.class;
//            } else if (Short.class == type) {
//                return short.class;
//            } else if (Byte.class == type) {
//                return byte.class;
//            } else if (Float.class == type) {
//                return float.class;
//            } else if (Character.class == type) {
//                return char.class;
//            } else if (Long.class == type) {
//                return long.class;
//            } else if (Double.class == type) {
//                return double.class;
//            } else if (Boolean.class == type) {
//                return boolean.class;
//            } else if (Void.class == type) {
//                return void.class;
//            }
//        }
//        return type;
//    }
}
