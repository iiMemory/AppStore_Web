package servlet;

import db.DButil;
import util.L;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class TestServlet extends javax.servlet.http.HttpServlet {
    protected void doPost(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        L.d("进入servlet啦~");


        String t = request.getParameter("t");

//        String msg = request.getParameter("msg");
//        Connection conn = DButil.getConnection();
//        try {
//            String sql = "INSERT INTO test(name) values (?)";// set age = ? , city = ?//逗号隔开
//            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
//            ptmt.setString(1,msg);
//            ptmt.execute();
//
//            response.getWriter().print("ok");
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }

        response.getWriter().print("{\""+t+"\"}");


    }

    protected void doGet(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response) throws javax.servlet.ServletException, IOException {
        doPost(request, response);
    }
}
