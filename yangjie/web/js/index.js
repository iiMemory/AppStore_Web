function login() {
    alert("login");
}

// 获取app列表信息
function loadAppList() {
    $.ajax({
        url:'MainServlet',
        type:'POST', //GET
        async:true,    //或false,是否异步
        timeout:5000,    //超时时间
        data: {
            action:"getAppList"
        },
        dataType:'json',    //返回的数据格式：json/xml/html/script/jsonp/text
        success:function(data,textStatus,jqXHR) {//data是成功后，接收的返回值
            // json转实体
            var length = data.length;
            var $table_tbody = $("#table_app tbody");
            $table_tbody.html("");
            var str = "";
            var column = 4;
            for (var i =1; i < length+1; i++) {
                if (i-1%column == 0) {
                    str += "<tr>";
                }
                str += "<td class='item_app'></td>";

                if (i%column == 0) {
                    str += "</tr>";
                }
            }
            $table_tbody.html(str);
        }
    });
}