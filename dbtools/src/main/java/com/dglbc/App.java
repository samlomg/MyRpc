package com.dglbc;

import com.dglbc.dbtools.ExecSql;
import com.dglbc.dbtools.SqlHelper;
import com.dglbc.dbtools.SqlKey;
import com.dglbc.dbtools.jdbc.JDBC;
import com.dglbc.dbtools.jdbc.face.IVo;
import com.dglbc.dbtools.join.Join;
import com.dglbc.dbtools.where.Where;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.ResultSet;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) throws Exception {
        Map<String, String> map = System.getenv();
        System.out.println(map.get("test_db"));
        HikariConfig config = new HikariConfig();
        config.setJdbcUrl("jdbc:sqlserver://192.168.22.226:1433; DatabaseName=ZBERP");
        config.setUsername("sa");
        config.setPassword(map.get("test_db"));

        config.addDataSourceProperty("cachePrepStmts", "true");
        config.addDataSourceProperty("prepStmtCacheSize", "250");
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
        HikariDataSource ds = new HikariDataSource(config);
        SqlHelper sqlHelper = new SqlHelper("F4201");
        Join f4211 = new Join(SqlKey.LEFTJOIN, "F4211", "B", "Sequence", "A", "Sequence");

        sqlHelper.sc(f4211, "SDLITM").sc("SHDOCO");
        sqlHelper.join(f4211);
        sqlHelper.where(new Where(SqlKey.AND).eq(f4211,"Sequence", 236536));
        ExecSql execSql = sqlHelper.selectBuilder();
        System.out.println(execSql.getSql());
        List<String> a= JDBC.list(ds.getConnection(), execSql.getSql(), new IVo<String>() {
            @Override
            public String row(ResultSet rs, int rowNum) throws Exception {
                return rs.getString(2);
            }
        },execSql.getValues().toArray());
        System.out.println(a.toString());
    }
}
