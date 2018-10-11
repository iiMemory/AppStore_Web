<%--
  Created by IntelliJ IDEA.
  User: Administrator
  Date: 2018/10/10
  Time: 18:29
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html; charset=utf-8"%>
<html>
<script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
<head>
</head>
<body>
<h2>Hello World</h2>
<input type="text" id="msg"/><button type="button"  style="width:100px;height:20px" onclick="start()">保存</button></br>
<input type="text" id="read"/><button type="button"  style="width:50px;height:20px">读取</button>
</body>

<script type="text/javascript" >
    function start(){
        var content=$("#msg").val();
        $.ajax({
            url:'TestServlet',
            type:'POST', //GET
            async:true,    //或false,是否异步
            data:{
                msg:content,age:25//想要传输过去的数据 key：value，另一个页面通过 key接收value的值
            },
            timeout:5000,    //超时时间
            dataType:'text',    //返回的数据格式：json/xml/html/script/jsonp/text
            success:function(data,textStatus,jqXHR){//data是成功后，接收的返回值
                console.log(data);
                console.log(textStatus);
                console.log(jqXHR);
                $("#read" +
                    "").val(data);//将返回成功的值展示到input里
            }
        });
    }
</script>
</html>

