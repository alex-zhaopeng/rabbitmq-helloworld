package com.SPI;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCTest {

    public static void main(String[] args) throws SQLException {
        String url = "jdbc:mysql://localhost:1521/mysql";
        Connection conn = DriverManager.getConnection(url,"root","Passw0rd");

        System.out.println(conn);
    }
}
