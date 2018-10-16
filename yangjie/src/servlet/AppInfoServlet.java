package servlet;

import bean.AppBean;
import bean.CommentBean;
import com.alibaba.fastjson.JSON;
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
import java.util.ArrayList;
import java.util.List;

public class AppInfoServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("getAppInfo")) {
            String packageId = request.getParameter("packageId");
            getAppInfo(packageId, response);
        } else if (action.equals("getCommentList")) {
            String packageId = request.getParameter("packageId");
            String page = request.getParameter("page");
            getCommentList(packageId, Integer.parseInt(page), response);
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    // 从数据库获取app信息
    private void getAppInfo(String packageId, HttpServletResponse response) throws IOException {
        L.d("开始执行getAppInfo...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "select * from appInfo where packageId = "+packageId;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            rs.next();
            AppBean bean = null;
                bean = new AppBean();
                bean.setPackageId(rs.getString("packageId"));
                bean.setName(rs.getString("name"));
                bean.setDownloadUrl(rs.getString("downloadUrl"));
                bean.setLogoUrl(rs.getString("logoUrl"));
                bean.setDescribe(rs.getString("describe"));
            // 对象转json
            String json = JSON.toJSONString(bean);
            response.getWriter().print(json);
            L.d("执行getAppInfo...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行getAppInfo...失败："+e.getMessage());
        }
    }

    // 从数据库获取评论信息
    private void getCommentList(String packageId, int page, HttpServletResponse response) throws IOException {
        L.d("开始执行getCommentList...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "select * from comment where packageId = "+packageId +" limit "+(page-1)*10+",10" ;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            List<CommentBean> list = new ArrayList<>(10);
            while (rs.next()) {
                CommentBean bean = null;
                bean = new CommentBean();
                bean.setId(rs.getString("id"));
                bean.setPackageId(rs.getString("packageId"));
                bean.setComment(rs.getString("comment"));
                bean.setUserId(rs.getString("userId"));
                bean.setTime(rs.getString("time"));
                list.add(bean);
            }
            // 对象转json
            String json = JSON.toJSONString(list);
            response.getWriter().print(json);
            L.d("执行getCommentList...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行getCommentList...失败："+e.getMessage());
        }
    }

}
