package db;

import util.L;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DButil {

//    private static final String URL = "jdbc:mysql://10.8.1.210:3306/yangjie?useUnicode=true&characterEncoding=utf-8";
private static final String URL = "jdbc:mysql://120.77.174.44:3306/yangjie?useUnicode=true&characterEncoding=utf-8";

    private static final String USER = "root";
    private static final String PASSWORD = "123";

    public static Connection conn = null;
    static {
        try {
            // 1.加载驱动程序
            Class.forName("com.mysql.jdbc.Driver");
            // 2. 获得数据库的连接
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            L.d("数据库连接成功！");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            L.d("数据库连接失败！"+e.getMessage());
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("数据库连接失败！"+e.getMessage());
        }
    }

    public static Connection getConnection() {
        return conn;
    }

}
