<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/25
  Time: 17:23
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/upload.js"></script>
    <link rel="stylesheet" href="css/upload.css" media="screen" type="text/css" />
    <title>上传应用jjjjjj</title>
</head>
<body>
<form action="UploadServlet" enctype="multipart/form-data" method="post">
    <div><input name="file" type="file" accept=".apk" /></div>
    <div><span>应用包名：</span><input type="text" name="packageId"/></div>
    <div><span>应用名称：</span><input type="text" name="name"/></div>
    <div><span>应用描述：</span><input type="text" name="describe"/></div>
    <input type="submit" value="提交"/>
</form>

</body>
</html>
