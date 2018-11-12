package api;

import bean.BaseResponse;
import bean.User;
import com.alibaba.fastjson.JSON;
import db.DButil;
import util.JavaWebTokenUtil;
import util.L;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

@Path("user")
public class UserApi {

    /**
     * 登录接口
     * @param userName 用户名
     * @param psw 密码
     * @return
     */
    @POST
    @Path("/login")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    public String login(@FormParam("userName") String userName,  @FormParam("psw") String psw) {
        Connection conn = DButil.getConnection();
        BaseResponse response = new BaseResponse<User>();
        try {
            String sql = "select * from user where name = '"+userName+"' and password = '"+psw+"'";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            if (rs.next()) {
                response.setCode(Code.SUCCESS);
                response.setMsg("登录成功");
                String token = createToken(userName);
                response.setData(new User(userName, token));
            } else {
                response.setCode(Code.FAILURE);
                response.setMsg("登录失败");
                response.setData(new User(userName));
            }
            L.d("执行login...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg(e.getMessage());
            L.d("执行login...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }

    // 创建token
    private String createToken(@FormParam("userName") String userName) {
        Map<String,Object> m = new HashMap<String,Object>();
        m.put("userName", userName); // 唯一值
        return JavaWebTokenUtil.createJavaWebToken(m);
    }

    /**
     * 注册接口
     * @param userName 用户名
     * @param psw 密码
     * @return
     */
    @POST
    @Path("/register")
    @Consumes("application/x-www-form-urlencoded")
    @Produces(MediaType.APPLICATION_JSON)
    public String register(@FormParam("userName") String userName, @FormParam("psw") String psw) {
        BaseResponse response = new BaseResponse<User>();
        Connection conn = DButil.getConnection();
        if (!checkCanRegister(userName)) {
            response.setCode(Code.FAILURE);
            response.setMsg("用户名已存在, 不允许注册");
            return JSON.toJSONString(response);
        }

        // 注册
        try {
            String sql = "insert  into  user(name, password) values (?,?)";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ptmt.setString(1,userName);
            ptmt.setString(2,psw);
            ptmt.execute();
            L.d("执行register...成功");
            response.setCode(Code.SUCCESS);
            response.setMsg("注册成功！");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg("注册失败！"+e.getMessage());
            L.d("执行register...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }

    // 是否允许注册
    private boolean checkCanRegister(String userName) {
        Connection conn = DButil.getConnection();
        boolean canRegister = false;//  能否注册
        // 能否注册，已存在userName则不允许注册
        try {
            String sql = "select * from user where name = '"+userName+"'";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            if (!rs.next()) {
                canRegister = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return canRegister;
    }
}
