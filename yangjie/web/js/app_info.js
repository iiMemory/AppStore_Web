// onload方法执行
// 获取app详情
function getAppInfo() {
     // 获取packageId
    // 进行ajax获取详情
    // todo 感觉这是不规范的写法！
    var url = location.href;
    var d = url.indexOf("=");
    var packageId = url.substring(d+1, url.length);

    $.ajax({
        url:'AppInfoServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            packageId:packageId
        },
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            var app_info_data = $("#app_info_data");
            // json转实体
            var str = "";
            var packageId = data.packageId;
            var name = data.name;
            var describe = data.describe;
             str+= "<p>应用包名:"+packageId+"</p>";
             str+= "<p>应用名称:"+name+"</p>";
             str+= "<p>应用简介:"+describe+"</p>";
             str+= "<button type=\"button\">下载</button>";
            app_info_data.html(str);
        }
    });
}