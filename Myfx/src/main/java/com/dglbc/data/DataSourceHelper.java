package com.dglbc.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class DataSourceHelper {
    public final static String DB_URL = "jdbc:derby:database;create=true";
    public final static String DERBY_DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";

    public static void main(String[] args) throws ClassNotFoundException, IllegalAccessException, InstantiationException, SQLException {


        Class.forName(DERBY_DRIVER).newInstance();
        System.out.println("Load the embedded driver");
        Connection conn = null;
        Statement stmt = null;
        Properties props = new Properties();
        props.put("user", "lbc");
        props.put("password", "1");
        // create and connect the database named helloDB
        try {
            conn = DriverManager.getConnection(DB_URL);
            conn.setAutoCommit(true);
            System.out.println("create and connect to database");
            stmt = conn.createStatement();
            String createSql = "CREATE TABLE %s (%s)";
            stmt.executeUpdate(String.format(createSql, "MEMBER", "id int primary key,name varchar(200),mobile varchar(20),email varchar(100)"));
            stmt.executeUpdate("Insert into MEMBER(id,name,mobile,email) values(1,'LBC','13652488432','youaadd@163.com')");

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            stmt.close();
            conn.close();
        }

    }
}
