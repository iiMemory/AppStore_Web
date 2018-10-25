package servlet;

import db.DButil;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
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
        upload(request);
    }

    // 上传
    private void upload(HttpServletRequest request) {
        String packageId = null;
        String name = null;
        String fileName = null;
        String describe = null;

        //得到上传文件的保存目录，将上传的文件存放于WEB-INF目录下，不允许外界直接访问，保证上传文件的安全
//        String savePath = this.getServletContext().getRealPath("/WEB-INF/upload");
        String savePath = this.getServletContext().getRealPath("/upload");
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
                    String filename = item.getName();
                    System.out.println(filename);
                    if (filename == null || filename.trim().equals("")) {
                        continue;
                    }
                    //注意：不同的浏览器提交的文件名是不一样的，有些浏览器提交上来的文件名是带有路径的，如：  c:\a\b\1.txt，而有些只是单纯的文件名，如：1.txt
                    //处理获取到的上传文件的文件名的路径部分，只保留文件名部分
                    filename = filename.substring(filename.lastIndexOf("\\") + 1);
                    fileName = filename;
                    //获取item中的上传文件的输入流
                    InputStream in = item.getInputStream();
                    //创建一个文件输出流
                    FileOutputStream out = new FileOutputStream(savePath + "\\" + filename);
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
            insertAppInfo(packageId, name, fileName, describe);
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
     * @return
     */
    // 将上传的文件信息插入数据库中
    private boolean insertAppInfo(String packageId, String name, String fileName, String describe) {
        L.d("开始执行insertAppInfo...");
        // 从数据库获取app信息
        Connection conn = DButil.getConnection();
        try {
            String sql = "insert  into  appInfo(packageId, name, fileName, describes) values (?,?,?,?)";
            PreparedStatement ptmt =  (PreparedStatement) conn.prepareStatement(sql);
            ptmt.setString(1,packageId);
            ptmt.setString(2,name);
            ptmt.setString(3,fileName);
            ptmt.setString(4,describe);
            ptmt.execute();
            L.d("执行insertAppInfo...成功");
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            L.d("执行insertAppInfo...失败："+e.getMessage());
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
