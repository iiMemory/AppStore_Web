function login() {
    var name = $("#input_user").val();
    var psw = $("#input_psw").val();

    $.ajax({
        url:'LoginServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            userName:name,
            password:psw,
        },
        dataType:'text',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            alert(data);
        },
        error:function (data,textStatus,jqXHR) {
            // todo 如何才能执行error的回调呢？？
            alert(data);
        }
    });
}