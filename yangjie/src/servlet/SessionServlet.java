package servlet;

import bean.User;
import util.L;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// 用于获取登录状态
public class SessionServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action.equals("loadUser")) {
            User user = (User) request.getSession().getAttribute("user");
            if (user != null) {
                response.getWriter().print(user.getName());
                L.d(user.getName());
            }
        } else if (action.equals("removeUser")) {
            //1.Session对象删除保存User对象
            request.getSession().removeAttribute("user");
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
         doPost(request, response);
    }
}
