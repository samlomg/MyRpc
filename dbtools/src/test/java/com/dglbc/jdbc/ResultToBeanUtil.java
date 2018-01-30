package com.dglbc.jdbc;

import com.dglbc.jdbc.exception.AppExp;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by LBC on 2017/5/3.
 * 忘记在哪里找到的代码
 */
public class ResultToBeanUtil<T> {
    /**
     * @param clazz 所要封装的javaBean
     * @param rs    记录集
     * @return ArrayList 数组里边装有 多个javaBean
     * @throws Exception
     */
    public List<T> getList(Class<T> clazz, ResultSet rs) {
        Field field = null;
        List<T> lists = new ArrayList<T>();
        try {
            // 取得ResultSet列名
            ResultSetMetaData rsmd = rs.getMetaData();
            // 获取记录集中的列数
            int counts = rsmd.getColumnCount();
            // 定义counts个String 变量
            String[] columnNames = new String[counts];
            // 给每个变量赋值(字段名称全部转换成大写)
            for (int i = 0; i < counts; i++) {
//                columnNames[i] = rsmd.getColumnLabel(i + 1).toUpperCase();
                columnNames[i] = rsmd.getColumnLabel(i + 1);
            }
            // 变量ResultSet
            while (rs.next()) {
                T t = clazz.newInstance();
                // 反射, 从ResultSet绑定到JavaBean
                for (int i = 0; i < counts; i++) {
                    // 设置参数类型，此类型应该跟javaBean 里边的类型一样，而不是取数据库里边的类型
                    field = clazz.getDeclaredField(columnNames[i]);
                    // 这里是获取bean属性的类型
                    Class<?> beanType = field.getType();
                    // 根据 rs 列名 ，组装javaBean里边的其中一个set方法，object 就是数据库第一行第一列的数据了
                    Object value = rs.getObject(columnNames[i]);
                    if (value != null) {
                        // 这里是获取数据库字段的类型
                        Class<?> dbType = value.getClass();
                        // 处理日期类型不匹配问题
                        if (dbType == java.sql.Timestamp.class && beanType == java.util.Date.class) {
                            // value = new
                            // java.util.Date(rs.getTimestamp(columnNames[i]).getTime());
                            value = new java.util.Date(
                                    ((java.sql.Timestamp) value).getTime());
                        }
                        // 处理double类型不匹配问题
                        if (dbType == java.math.BigDecimal.class
                                && beanType == double.class) {
                            // value = rs.getDouble(columnNames[i]);
                            value = new Double(value.toString());
                        }
                        // 处理int类型不匹配问题
                        if (dbType == java.math.BigDecimal.class
                                && beanType == int.class) {
                            // value = rs.getInt(columnNames[i]);
                            value = new Integer(value.toString());
                        }
                    }

                    String setMethodName = "set" + firstUpperCase(columnNames[i]);
                    // 第一个参数是传进去的方法名称，第二个参数是 传进去的类型；
                    Method m = t.getClass().getMethod(setMethodName, beanType);

                    // 第二个参数是传给set方法数据；如果是get方法可以不写
                    m.invoke(t, value);
                }
                lists.add(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return lists;
    }

    /**
     * @param clazz bean类
     * @param rs    结果集 (只有封装第一条结果)
     * @return 封装了查询结果的bean对象
     */
    public T getObj(Class<T> clazz, ResultSet rs) {
        T obj = null;
        try {
            // 变量ResultSet
            if (rs.next()) {
                //初始化T
                T t = clazz.newInstance();
                // 取得ResultSet列名
                ResultSetMetaData rsmd = rs.getMetaData();

                // 获取记录集中的列数
                int counts = rsmd.getColumnCount();
                Set<String> result = new HashSet<>();
                Map<String, String> dateColumns = new HashMap<>();
                // 自定义对应数据库字段
                // 给每个变量赋值(字段名称全部转换成大写)
                for (int i = 0; i < counts; i++) {
                    String tmp = rsmd.getColumnLabel(i + 1);
                    dateColumns.put(tmp.toUpperCase(), tmp);
                }
                Map<String, Field> beanNames = new HashMap<>();
                //获取T所有的类型
                Field[] fields = clazz.getFields();
                for (int i = 0; i < fields.length; i++) {
                    beanNames.put(fields[i].getName().toUpperCase(), fields[i]);
                }
                //获取交集（得到一定有的）
                result.addAll(dateColumns.keySet());
                result.retainAll(beanNames.keySet());
                for (String name : result) {
                    Field field = beanNames.get(name);
//                    rs.getObject(dateColumns.get(name), ReflectUntil.wrapper(field.getType())
                    if (!setValue(field, name,getObject(dateColumns.get(name),field.getType(),rs,dateColumns))) {
                        AppExp.error(name+"赋值失败");
                    }
//                String getMethodName = "get";
//                String setMethodName = "set" + firstUpperCase(name);
//                Method g = rs.getClass().getMethod(getMethodName);
//                Object beanValue = g.invoke(rs);
//            String setMethodName = "set" + firstUpperCase(parmerName);
//            // 第一个参数是传进去的方法名称，第二个参数是 传进去的类型；
//            Method m = t.getClass().getMethod(setMethodName, fields[i].getType());
//
//            // 第二个参数是传给set方法数据；如果是get方法可以不写
//            m.invoke(t, rs.getObject(parmerName));
                }
                obj = t;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return obj;
    }

    // 首写字母变大写  这里大家视情况而定，保证数据库和你的Javabean字段对应即可
    public static String firstUpperCase(String old) {
        return old.substring(0, 1).toUpperCase() + old.substring(1);
    }

    //处理已知类型不匹配问题
//    public Object typeCompose(Object value, Class<?> beanType) {
//        if (value != null) {
//
//            // 这里是获取数据库字段的类型
//            Class<?> dbType = value.getClass();
//            // 处理日期类型不匹配问题
//            if (dbType == java.sql.Timestamp.class
//                    && beanType == java.util.Date.class) {
//                value = new java.util.Date(
//                        ((java.sql.Timestamp) value).getTime());
//            }
//            // 处理double类型不匹配问题
//            if (dbType == java.math.BigDecimal.class
//                    && beanType == double.class) {
//                value = new Double(value.toString());
//            }
//            // 处理int类型不匹配问题
//            if (dbType == java.math.BigDecimal.class
//                    && (beanType == int.class || beanType == Integer.class)) {
//                value = new Integer(value.toString());
//            }
//
//            if (dbType == java.math.BigDecimal.class
//                    && beanType == String.class) {
//                value = new Integer(value.toString()) + "";
//            }
//        }
//        return value;
//    }

    public boolean setValue(Field f, String name, Object object) {
        boolean re = true;
        try {
            f.setAccessible(true);
            f.set(name, object);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            re = false;
        }
        return re;
    }


    public Object getObject(String Columnname,Class<?> beanType,ResultSet rs,Map<String, String> dateColumns) throws SQLException, IllegalAccessException, InstantiationException {
        Object o = null;
        if (ReflectUntil.isJavaClass(beanType)){
            //java 内置类型class
            o = rs.getObject(dateColumns.get(Columnname), ReflectUntil.wrapper(beanType));
        }else {
            o=beanType.newInstance();
            // 获取记录集中的列数
            Set<String> result = new HashSet<>();
            // 自定义对应数据库字段
            Map<String, Field> beanNames = new HashMap<>();
            //获取T所有的类型
            Field[] fields = beanType.getFields();
            for (int i = 0; i < fields.length; i++) {
                beanNames.put(fields[i].getName().toUpperCase(), fields[i]);
            }
            //获取交集（得到一定有的）
            result.addAll(dateColumns.keySet());
            result.retainAll(beanNames.keySet());
            for (String name : result) {
                Field field = beanNames.get(name);
                if (!setValue(field, name, getObject(dateColumns.get(name),field.getType(),rs,dateColumns))) {
                    AppExp.error(name+"赋值失败");
                }
//                String getMethodName = "get";
//                String setMethodName = "set" + firstUpperCase(name);
//                Method g = rs.getClass().getMethod(getMethodName);
//                Object beanValue = g.invoke(rs);
//            String setMethodName = "set" + firstUpperCase(parmerName);
//            // 第一个参数是传进去的方法名称，第二个参数是 传进去的类型；
//            Method m = t.getClass().getMethod(setMethodName, fields[i].getType());
//
//            // 第二个参数是传给set方法数据；如果是get方法可以不写
//            m.invoke(t, rs.getObject(parmerName));
            }
        }
        return null;
    }
}
