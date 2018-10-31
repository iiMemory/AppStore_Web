package api;

import bean.BaseResponse;
import bean.CommentBean;
import bean.MobileAppInfo;
import bean.MobileAppItem;
import com.alibaba.fastjson.JSON;
import db.DButil;
import util.Constant;
import util.L;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

@Path("app")
public class AppApi {

    /**
     * 获取app列表
     * @param page 页码
     * @param row 行数
     * @return
     */
    @GET
    @Path("/queryList/{page}/{row}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    public String queryList(@PathParam("page") String page, @PathParam("row") String row) {
        Connection conn = DButil.getConnection();
        BaseResponse response = new BaseResponse<List<MobileAppItem>>();
        int p = Integer.parseInt(page);
        try {
            String sql = "select * from appInfo limit "+(p-1)*10+","+row;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            List<MobileAppItem> list = new ArrayList<>();
            MobileAppItem bean = null;
            while (rs.next()) {
                bean = new MobileAppItem();
                bean.setAppName(rs.getString("name"));
                bean.setPackageId(rs.getString("packageId"));
                bean.setDownloadUrl(Constant.uploadPath+"/"+rs.getString("fileName"));
                bean.setLogoPicUrl(Constant.uploadPath+"/"+rs.getString("logoPicName"));
                list.add(bean);
            }
            response.setCode(Code.SUCCESS);
            response.setMsg("获取app列表成功");

            response.setData(list);
            L.d("执行queryList...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg(e.getMessage());
            L.d("执行queryList...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }


    /**
     * 获取app详情
     * @param packageId
     * @return
     */
    @GET
    @Path("/query/{packageId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    public String query(@PathParam("packageId") String packageId) {
        Connection conn = DButil.getConnection();
        BaseResponse response = new BaseResponse<MobileAppInfo>();
        try {
            String sql = "select * from appInfo where packageId = '"+packageId+"'";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            MobileAppInfo info = new MobileAppInfo();
            if (rs.next()) {
                info.setAppName(rs.getString("name"));
                info.setLogoPicUrl(Constant.uploadPath+"/"+rs.getString("logoPicName"));
                info.setAppDescribes(rs.getString("describes"));
                info.setScreenShotPicUrl_1(Constant.uploadPath+"/"+rs.getString("screenShotPicName_1"));
                info.setScreenShotPicUrl_2(Constant.uploadPath+"/"+rs.getString("screenShotPicName_2"));
                info.setScreenShotPicUrl_3(Constant.uploadPath+"/"+rs.getString("screenShotPicName_3"));
                info.setDownloadUrl(Constant.uploadPath+"/"+rs.getString("fileName"));
                response.setCode(Code.SUCCESS);
                response.setMsg("获取app详情成功");
                response.setData(info);
            } else {
                response.setCode(Code.SUCCESS);
                response.setMsg("没有查询到该包名的应用");
            }
            L.d("执行query...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg(e.getMessage());
            L.d("执行query...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }

    @GET
    @Path("/queryCommentList/{packageId}/{page}/{row}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    public String queryList(@PathParam("packageId") String packageId, @PathParam("page") String page, @PathParam("row") String row) {
        Connection conn = DButil.getConnection();
        BaseResponse response = new BaseResponse<List<CommentBean>>();
        int p = Integer.parseInt(page);
        try {
            String sql = "select * from comment where packageId = '"+packageId+"' limit "+(p-1)*10+","+row;
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ResultSet rs = ptmt.executeQuery();
            List<CommentBean> list = new ArrayList<>();
            CommentBean bean = null;
            while (rs.next()) {
              bean = new CommentBean();
              bean.setUserName(rs.getString("userName"));
              bean.setTime(rs.getString("time"));
              bean.setComment(rs.getString("comment"));
              list.add(bean);
            }
            response.setCode(Code.SUCCESS);
            response.setMsg("获取评论列表成功");
            response.setData(list);
            L.d("执行queryList...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg(e.getMessage());
            L.d("执行queryList...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }

    @POST
    @Path("/comment")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes("application/x-www-form-urlencoded")
    public String comment(@FormParam("packageId") String packageId, @FormParam("userName") String userName, @FormParam("comment") String comment) {
        Connection conn = DButil.getConnection();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");//设置日期格式
        String time = df.format(new java.util.Date());
        BaseResponse response = new BaseResponse<List<CommentBean>>();
        try {
            String sql = "insert into comment(userName, packageId, comment, time) values(?,?,?,?) ";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ptmt.setString(1, userName);
            ptmt.setString(2, packageId);
            ptmt.setString(3, comment);
            ptmt.setString(4, time);
            ptmt.execute();
            response.setCode(Code.SUCCESS);
            response.setMsg("评论成功");
            L.d("执行comment...成功");
        } catch (SQLException e) {
            e.printStackTrace();
            response.setCode(Code.FAILURE);
            response.setMsg("评论失败"+e.getMessage());
            L.d("执行comment...失败："+e.getMessage());
        }
        return JSON.toJSONString(response);
    }

}
