package util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Created by admin on 2017/5/25.
 */
public class DBConn {

    private static Connection conn;
    public static Connection getConn(){
        try {
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try {
            String url = "jdbc:mysql://localhost:3306/dbdemo";
            conn = DriverManager.getConnection(url, "root", "root");
            if (conn != null) {
                System.out.println("数据库连接成功！");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return conn;
    }

    public static void closeConn(Connection conn) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

}
