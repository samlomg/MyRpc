package com.dglbc.jdbc;

import com.dglbc.entity.BaseFrom;
import com.dglbc.tool.CustomerStringUntils;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;

public class AutoJava  {
    public List<BaseFrom> getBase(ResultSet rs) throws SQLException {
        // 取得ResultSet列名
        ResultSetMetaData rsmd = rs.getMetaData();
        // 获取记录集中的列数
        int counts = rsmd.getColumnCount();
        // 定义counts个String 变量
        BaseFrom[] columnNames = new BaseFrom[counts];
        // 获取
        for (int i = 0; i < counts; i++) {
            columnNames[i] = new BaseFrom(rsmd.getColumnType(i+1),CustomerStringUntils.firstLow(rsmd.getColumnLabel(i + 1)));
        }

        return null;
    }


}
