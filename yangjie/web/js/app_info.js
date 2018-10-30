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
            app_info_data.html("");
            // json转实体
            var str = "";
            var packageId = data.packageId;
            var name = data.name;
            var describe = data.describes;
            var fileName = data.fileName;
             str+= "<p>应用包名:  "+packageId+"</p>";
             str+= "<p>应用名称:  "+name+"</p>";
             str+= "<p>应用简介:</p><p id='describe'>"+describe+"</p>";
             str+= "<a id='download' href=\"/upload/"+fileName+"\">下载</a>";
            app_info_data.html(str);

            //    应用截图
            var screenshot_pic_list = $("#screenshot_pic_list");
            var ht = "";
            screenshot_pic_list.html("");
            var screenshot_pic_1 = data.screenShotPicName_1;
            var screenshot_pic_2 = data.screenShotPicName_2;
            var screenshot_pic_3 = data.screenShotPicName_3;
            ht += "<div>";
            if ($.trim(screenshot_pic_1)) {
                ht += "<img src=\"/upload/"+screenshot_pic_1+"\" />";
            }
            if ($.trim(screenshot_pic_2)) {
                ht += "<img src=\"/upload/"+screenshot_pic_2+"\" />";
            }
            if ($.trim(screenshot_pic_3)) {
                ht += "<img src=\"/upload/"+screenshot_pic_3+"\" />";
            }
            ht += "</div>";
            screenshot_pic_list.html(ht);

            // 应用图标
            var img_logo = $("#img_app");
            var logo = data.logoPicName;
            img_logo.attr("src","/upload/"+logo+"");
            // img_logo.src = "/upload/"+logo+"";
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
            comment_list.html("");
            // json转实体
            var length = data.length;
            var str = "";
            for (var i = 0; i < length; i++) {
                var userName = data[i].userName;
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

// 提交评论
function commitComment() {
    var url = location.href;
    var d = url.indexOf("=");
    var packageId = url.substring(d+1, url.length);

    var comment = $("#text_comment").val();
    $.ajax({
        url:'AppInfoServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            comment:comment,
            action:"commitComment",
            packageId:packageId
        },
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
          var code = data.code;
          if (code == 200) {
              loadInfo();
          }
          alert(data.msg);
        },
        error:function (data,textStatus,jqXHR) {
            alert(data);
        }
    });

}