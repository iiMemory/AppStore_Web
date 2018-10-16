function login() {
    window.location.href='../web/login.html';
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
                var dd = data[i-1].packageId;
                str += "<td class='item_app' onclick=clickAppItem("+dd+")></td>";

                if (i%column == 0) {
                    str += "</tr>";
                }
            }
            $table_tbody.html(str);
        }
    });
}

// 点击app列表，跳转到详情界面
function clickAppItem(param) {
    window.location.href='../web/appInfo.html?packageId='+param;
}