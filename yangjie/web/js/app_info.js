// onload方法执行
function loadInfo() {
    getAppInfo();
    getCommentList();
}


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
            action:"getAppInfo",
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

function getCommentList() {
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
            page:"1",
            action:"getCommentList",
            packageId:packageId
        },
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            var comment_list = $("#comment_list");
            // json转实体
            var length = data.length;
            var str = "";
            for (var i = 0; i < length; i++) {
                var userName = data[i].userId;
                var comment = data[i].comment;
                var date = data[i].time;
                str += "<div class=\"comment_div\">\n" +
                    "                <div>\n" +
                    "                <span>";
                str += userName;
                str += "</span>\n" +
                    "                <span class=\"comment_date\">";
                str += date;
                str += " </span>\n" +
                    "            </div>\n" +
                    "            <span class=\"comment_msg\">";
                str += comment;
                str += "</span>\n" +
                "                </div>";
            }
            comment_list.html(str);
        }
    });
}