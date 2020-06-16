package com.dglbc.jdbc;

import com.dglbc.dbtools.Express;
import com.dglbc.dbtools.produce.ProduceParameter;
import com.dglbc.jdbc.exception.MultiRowExp;
import com.dglbc.jdbc.exception.SqlExp;
import com.dglbc.jdbc.face.ICallResult;
import com.dglbc.jdbc.face.IVo;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.commons.lang3.StringUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by LBC on 2017/12/19
 **/

public class JDBC {

    public static Connection getConnection() throws SQLException {
        Map<String, String> map = System.getenv();
        HikariConfig config = new HikariConfig("/hikari.properties");
        config.setPassword(map.get("db_test"));
        HikariDataSource ds = new HikariDataSource(config);
        return ds.getConnection();
    }

    public static <T> T get(Connection con, String sql, IVo<T> iQuery, Object... params) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            SqlExp.error("错误:查询相关的sql语句为空");
        }
        PreparedStatement ptm = null;
        ResultSet rs = null;
        T row = null;
        int rowCount = 0;
        try {
            ptm = con.prepareStatement(sql);
            int param_num = params.length;
            for (int i = 0; i < param_num; i++) {
                ptm.setObject(i + 1, params[i]);
            }
            rs = ptm.executeQuery();

            while (rs.next()) {
                if (rowCount == 0) {
                    row = iQuery.row(rs, 0);// 用户使用ResultSet对数据进行包装,返回vo对象
                }
                rowCount++;
            }
            if (rowCount > 1) {
                MultiRowExp.error("错误:查询的数据大于1条");
            }
        } catch (Exception e) {
            throw e; // 抛出异常,交给调用方处理,关闭连接
        } finally {
            JDBC.close(rs, null, ptm, null);// 连接交给调用方处理
        }

        return row;
    }

    public static <T> List<T> list(Express express, IVo<T> iQuery) throws Exception {
        return list(getConnection(), express.getSql().toString(), iQuery, express.getValues().toArray());
    }

    public static <T> List<T> list(Connection con, String sql, IVo<T> iQuery, Object... params) throws Exception {
        if (StringUtils.isEmpty(sql)) {
            SqlExp.error("错误:查询相关的sql语句为空");
        }
        ArrayList<T> list = null;// 结果集
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            ptm = con.prepareStatement(sql);
            int param_num = params.length;
            for (int i = 0; i < param_num; i++) {
                ptm.setObject(i + 1, params[i]);
            }
            rs = ptm.executeQuery();
            boolean init = false;
            int i = 0;
            T row = null;
            while (rs.next()) {
                if (!init) {
                    init = true;
                    list = new ArrayList<T>();
                }
                row = iQuery.row(rs, i);// 用户使用ResultSet对数据进行包装,返回vo对象
                if (row != null) {
                    list.add(row);// 如果不为空,则添加数据(用户可以根据业务需要,返回null,此行的数据将不添加到list中)
                }
                i++;
            }
        } catch (Exception e) {
            throw e; // 抛出异常,交给调用方处理,关闭连接
        } finally {
            JDBC.close(rs, null, ptm, null);// 连接交给调用方处理
        }
        return list;
    }

    public <T> T call(Connection con, String sql_callProcedure, boolean flag, ICallResult<T> iCallResult, ProduceParameter... params) throws Exception {
        /**
         * {call 过程名[(?, ?, ...)]} 　　返回结果参数的过程的语法为： {? = call 过程名[(?, ?, ...)]}
         * 　　不带参数的已储存过程的语法类似： {call 过程名}
         */
        CallableStatement cst = null;
        boolean ok = false;
        T row = null;
        try {
            cst = con.prepareCall(sql_callProcedure);
            // 设置参数

            int param_num = params.length;
            for (int i = 0; i < param_num; i++) {
                switch (params[i].getMode()) {
                    case IN:
                        cst.setObject(flag ? params[i].getNum() : i + 1, params[i].getValue());
                        break;
                    case OUT:
                        cst.registerOutParameter(flag ? params[i].getNum() : i + 1, (Integer) params[i].getValue());
                        break;
                    case INOUT:
                        cst.setObject(flag ? params[i].getNum() : i + 1, params[i].getValue());
                        cst.registerOutParameter(flag ? params[i].getNum() : i + 1, (Integer) params[i].getValue());
                        break;
                }
            }
            cst.executeUpdate();
            row = iCallResult.callResult(cst); // 获取结果
        } catch (Exception e) {
            throw e;
        } finally {
            JDBC.close(cst);
        }
        return row;
    }

    public static void close(ResultSet rs, Statement smt, PreparedStatement pmt, Connection con) {
        close(rs);
        close(smt);
        close(pmt);
        close(con);
    }

    public static void close(Connection con) {
        try {
            if (con != null) {
                con.close();
                con = null;
            }
        } catch (Exception ex) {

        }
    }

    public static void close(Statement smt) {
        try {
            if (smt != null) {
                smt.close();
                smt = null;
            }
        } catch (Exception ex) {

        }
    }

    public static void close(PreparedStatement pmt) {
        try {
            if (pmt != null) {
                pmt.close();
                pmt = null;
            }
        } catch (Exception ex) {

        }
    }

    public static void close(CallableStatement casmt) {
        try {
            if (casmt != null) {
                casmt.close();
                casmt = null;
            }
        } catch (Exception ex) {

        }
    }

    public static void close(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
                rs = null;
            }
        } catch (Exception ex) {

        }
    }
}
