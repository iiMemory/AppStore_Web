package servlet;

import db.DButil;
import util.L;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // 解决中文输出为？？问题
        response.setContentType("text/html;charset=GBK");
        response.setContentType("text/html");

        String userName = request.getParameter("userName");
        String password = request.getParameter("password");
        boolean isLoginSuccess = login(userName, password);
        if (isLoginSuccess) {
            // todo 保存Cookie or Session
            response.getWriter().print("登录成功！");
        } else {
            boolean canRegister = checkCanRegister(userName, password);
            if (canRegister) {
                boolean isRegisterSuccess = register(userName, password);
                if (isRegisterSuccess) {
                    // todo 保存Cookie or Session
                    response.getWriter().print("注册成功！");
                } else {
                    response.getWriter().print("注册失败！未知原因，请联系管理员");
                }
            } else {
                response.getWriter().print("注册失败，用户名已存在！");
            }
        }

    }

    // 注册
    private boolean register(String userName, String password) {
        L.d("开始执行register...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "insert  into  user(name, password) values (?,?)";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ptmt.setString(1,userName);
            ptmt.setString(2,password);
            ptmt.execute();
            L.d("执行register...成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行register...失败："+e.getMessage());
        }
        return false;
    }

    // 检查是否可以注册
    private boolean checkCanRegister(String userName, String password) {
        L.d("开始执行checkCanRegister...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "select * from user where name = "+userName;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                return false;
            }
            L.d("执行checkCanRegister...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行checkCanRegister...失败："+e.getMessage());
        }
        return true;
    }

    // 登录
    private boolean login(String userName, String password) {
        L.d("开始执行login...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "select password from user where name = "+userName;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            if (!rs.next()) {
                return false;
            }
            String psw = rs.getString("password");
            if (password.equals(psw)) {
                return true;
            }
            L.d("执行login...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行login...失败："+e.getMessage());
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
