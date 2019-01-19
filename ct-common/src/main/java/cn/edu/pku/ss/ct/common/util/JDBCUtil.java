package cn.edu.pku.ss.ct.common.util;

import java.sql.Connection;
import java.sql.DriverManager;

public class JDBCUtil {
    private static final String MARIADB_DRIVER_CLASS = "org.mariadb.jdbc.Driver";
    private static final String MARIADB_URL = "jdbc:mariadb://hadoop00:3306/ct?useUnicode=true&characterEncoding=UTF-8";
    private static final String MARIADB_USERNAME = "root";
    private static final String MARIADB_PASSWORD = "0817";

    public static Connection getConnection() {
        Connection connection = null;

        try {
            Class.forName(MARIADB_DRIVER_CLASS);
            connection = DriverManager.getConnection(MARIADB_URL, MARIADB_USERNAME, MARIADB_PASSWORD);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return connection;
    }
}
