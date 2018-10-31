package servlet;

import db.DButil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import util.CommonUtil;
import util.Constant;
import util.L;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

// 上传
public class UploadServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        upload(request, response);
    }

    // 上传
    private void upload(HttpServletRequest request, HttpServletResponse response) {
        String packageId = null;
        String name = null; // app名字
        String describe = null;
        String fileName = null; // 储存的apk名字，不用原来的，因为可能会重复！
        String logoPicName = null; // 存储的图标名字
        String screenShotPicName_1 = null; // 存储的截图名字
        String screenShotPicName_2 = null;
        String screenShotPicName_3 = null;


        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
//        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        String savePath = this.getServletContext().getRealPath(Constant.uploadPath);
        File file = new File(savePath);
        //判断上传文件的保存目录是否存在
        if (!file.exists() && !file.isDirectory()) {
            L.d(savePath + "目录不存在，需要创建");
            //创建目录
            file.mkdir();
        }
        //消息提示
        String message = "";

        try {
            //使用Apache文件上传组件处理文件上传步骤：
            //1、创建一个DiskFileItemFactory工厂
            DiskFileItemFactory factory = new DiskFileItemFactory();
            //2、创建一个文件上传解析器
            ServletFileUpload upload = new ServletFileUpload(factory);
            //解决上传文件名的中文乱码
            upload.setHeaderEncoding("UTF-8");
            //3、判断提交上来的数据是否是上传表单的数据
            if (!ServletFileUpload.isMultipartContent(request)) {
                //按照传统方式获取数据
                return;
            }
            //4、使用ServletFileUpload解析器解析上传数据，解析结果返回的是一个List<FileItem>集合，每一个FileItem对应一个Form表单的输入项
            List<FileItem> list = upload.parseRequest(request);
            for (FileItem item : list) {
                //如果fileitem中封装的是普通输入项的数据
                if (item.isFormField()) {
                    String fieldName = item.getFieldName();
                    //解决普通输入项的数据的中文乱码问题
                    String value = item.getString("UTF-8");
                    //value = new String(value.getBytes("iso8859-1"),"UTF-8");
                    System.out.println(fieldName + "=" + value);

                    if (fieldName.equals("packageId")) {
                        packageId = value;
                    }
                    if (fieldName.equals("name")) {
                        name = value;
                    }
                    if (fieldName.equals("describe")) {
                        describe = value;
                    }

                } else {//如果fileitem中封装的是上传文件
                    //得到上传的文件名称，
                    String fName = item.getName();
                    System.out.println(fName);
                    if (fName == null || fName.trim().equals("")) {
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    fName = fName.substring(fName.lastIndexOf("\\") + 1);
                    String tempName = fName;

                    String fieldName = item.getFieldName();
                    String stamp = CommonUtil.getStampTime();
                    if (fieldName.equals("fileName")) {
                        fName = stamp+"_fileName_"+tempName;
                        fileName = fName;
                    }
                    if (fieldName.equals("logoName")) {
                        fName = stamp+"_logoName_"+tempName;
                        logoPicName = fName;
                    }
                    if (fieldName.equals("screenShotPicName_1")) {
                        fName = stamp+"_screenShotPicName_1_"+tempName;
                        screenShotPicName_1 = fName;
                    }
                    if (fieldName.equals("screenShotPicName_2")) {
                        fName = stamp+"_screenShotPicName_2_"+tempName;
                        screenShotPicName_2 = fName;
                    }
                    if (fieldName.equals("screenShotPicName_3")) {
                        fName = stamp+"_screenShotPicName_3_"+tempName;
                        screenShotPicName_3 = fName;
                    }

                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(savePath + "\\" + fName);
                    //创建一个缓冲区
                    byte buffer[] = new byte[1024];
                    //判断输入流中的数据是否已经读完的标识
                    int len = 0;
                    //循环将输入流读入到缓冲区当中，(len=in.read(buffer))>0就表示in里面还有数据
                    while ((len = in.read(buffer)) > 0) {
                        //使用FileOutputStream输出流将缓冲区的数据写入到指定的目录(savePath + "\\" + filename)当中
                        out.write(buffer, 0, len);
                    }
                    //关闭输入流
                    in.close();
                    //关闭输出流
                    out.close();
                    //删除处理文件上传时生成的临时文件
                    item.delete();
                    message = "文件上传成功！";
                }
            }
            // 将上传的文件信息插入数据库中
            insertAppInfo(packageId, name, fileName, describe, logoPicName, screenShotPicName_1, screenShotPicName_2, screenShotPicName_3, response);
        } catch (Exception e) {
            message = "文件上传失败！";
            e.printStackTrace();
        } finally {
            L.d(message);
        }
    }

    /**
     *
     * @param packageId 包名
     * @param name app名
     * @param fileName 文件名
     * @param describe app描述
     * @param logoPicName logo名字
     * @param screenShotPicName_1 截图名字
     * @param screenShotPicName_2
     * @param screenShotPicName_3
     * @return
     */
    // 将上传的文件信息插入数据库中
    private boolean insertAppInfo(String packageId, String name, String fileName, String describe, String logoPicName, String screenShotPicName_1, String screenShotPicName_2, String screenShotPicName_3, HttpServletResponse response) throws IOException{
        L.d("开始执行insertAppInfo...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "insert  into  appInfo(packageId, name, fileName, describes, logoPicName, screenShotPicName_1, screenShotPicName_2, screenShotPicName_3) values (?,?,?,?,?,?,?,?)";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ptmt.setString(1,packageId);
            ptmt.setString(2,name);
            ptmt.setString(3,fileName);
            ptmt.setString(4,describe);
            ptmt.setString(5,logoPicName);
            ptmt.setString(6,screenShotPicName_1);
            ptmt.setString(7,screenShotPicName_2);
            ptmt.setString(8,screenShotPicName_3);
            ptmt.execute();
            L.d("执行insertAppInfo...成功");
            // 成功回调
            response.getWriter().print(Constant.successCode);
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行insertAppInfo...失败："+e.getMessage());
            response.getWriter().print(e.getMessage());
        }
        return false;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    private interface IUploadListener{
        void onSuccess();
        void onFailure();
    }
}
