package servlet;

import bean.AppBean;
import bean.User;
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

// 首页对于的servlet
public class MainServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("getAppList")) {
            getAppList(response);
//            // todo 模拟数据
//            List<AppBean> list = new ArrayList<>();
//            for (int i = 0; i < 8; i++) {
//                AppBean appBean = new AppBean();
//                appBean.setPackageId("com.a.b");
//                list.add(appBean);
//            }
//            // 对象转json
//            String json = JSON.toJSONString(list);
//            response.getWriter().print(json);
        }
    }

    // 从数据库获取app信息
    private void getAppList(HttpServletResponse response) throws IOException {
        L.d("开始执行getAppList...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "select * from appInfo";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            List<AppBean> list = new ArrayList<>();
            AppBean bean = null;
            while(rs.next()) {
                bean = new AppBean();
                bean.setPackageId(rs.getString("packageId"));
                bean.setName(rs.getString("name"));
                bean.setDownloadUrl(rs.getString("downloadUrl"));
                bean.setLogoUrl(rs.getString("logoUrl"));
                bean.setFileName(rs.getString("fileName"));
                list.add(bean);
            }
            // 对象转json
            String json = JSON.toJSONString(list);
            response.getWriter().print(json);
            L.d("执行getAppList...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行getAppList...失败："+e.getMessage());
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }
}
