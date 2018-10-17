
function loadUser() {
    $.ajax({
        url:'SessionServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            action:"loadUser"
        },
        dataType:'text',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            // json转实体
            if (data != "") {
                var div_user = $("#div_user").css("display", "block");
                var login = $("#login").css("display", "none");
                var user = $("#user");
                user.html(data + " ，已登录");
            }
        }
    });
}

function exit() {
    $.ajax({
        url:'SessionServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            action:"removeUser"
        },
        dataType:'text',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            // json转实体
                var div_user = $("#div_user").css("display", "none");
                var login = $("#login").css("display", "block");
        }
    });
}