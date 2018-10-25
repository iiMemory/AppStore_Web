// 跳转到登录页
function login() {
    window.location.href='../login.html';
}

// 读取登录信息
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
                if (data == "admin") {
                    var upload = $("#upload").css("display", "block");
                }
                user.html(data + " ，已登录");
            }
        }
    });
}
// 登出
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

// 上传页
function upLoad() {
    window.location.href='../upload.jsp';

}