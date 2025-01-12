package br.com.db;

import br.com.exceptions.DbException;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class DB {

    private static Connection conn = null;

    public static Connection getConnectio() {
        try {
            Properties props = loadProperties();
            if (props == null) {
                throw new IllegalStateException("Props was null");
            }
            String dburl = props.getProperty("dburl");
            conn = DriverManager.getConnection(dburl, props);
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        return conn;
    }

    public static void closeResultSet(ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    public static void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
    }

    private static Properties loadProperties() {
        try (BufferedReader br = new BufferedReader(new FileReader("db.properties"))) {
            Properties props = new Properties();
            props.load(br);
            return props;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
