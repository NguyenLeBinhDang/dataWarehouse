package service;

import model.Constant;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
    //    String database = "staging_db";
    private static Constant constant = new Constant();

    public static Connection getConnection(String database) {
        Connection c = null;

        try {
//            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            DriverManager.registerDriver(new com.mysql.cj.jdbc.Driver());

            String url = "jdbc:mySQL://localhost:3307/" + database;
//            String user = "root";
//            String pwd = "1234";
            String user = "admin";
            String pwd = "admin";

            c = DriverManager.getConnection(url, constant.ADMIN_USER, constant.ADMIN_PWD);

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return c;
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
