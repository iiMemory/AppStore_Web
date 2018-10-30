$(function(){
    var options = {
        beforeSubmit: validate,
        success:showResponse,
        dataType: 'text',
        error : function(xhr, status, err) {
            alert("操作失败");
        }
    };
    $("#form_upload").submit(function(){
        $(this).ajaxSubmit(options);
        return false;   //防止表单自动提交
    });
});

/**
 * 保存后，执行回调
 * @param responseText
 * @param statusText
 * @param xhr
 * @param $form
 */
// todo 优化回调信息
function showResponse(responseText, statusText, xhr, $form){
    if(responseText == "200"){
        alert('上传成功！');
        window.location.href='../index.html';
    } else {
        alert(responseText);
    }
}

// 检查输入框内容合法性
function validate(formData, jqForm, options) {
    var file = $('#fileName').val();
    var packageId = $('#packageId').val();
    var name = $('#name').val();
    var describe = $('#describe').val();
    var logoName =  $('#logoName').val();
    var screenShotPicName_1 =  $('#screenShotPicName_1').val();
    var screenShotPicName_2 =  $('#screenShotPicName_2').val();
    var screenShotPicName_3 =  $('#screenShotPicName_3').val();
    if (!$.trim(file)) {
        alert("请选择要上传的文件！")
        return false;
    }
    if (!$.trim(packageId)) {
        alert("请输入应用包名！");
        return false;
    }

    if (!$.trim(logoName)) {
        alert("请选择应用图标！");
        return false;
    }
    if (!$.trim(name)) {
        alert("请输入应用名字！");
        return false;
    }
    if (!$.trim(describe)) {
        alert("请输入应用描述！");
        return false;
    }

    // 至少上传1张应用截图
    if (!$.trim(screenShotPicName_1) && !$.trim(screenShotPicName_2) && !$.trim(screenShotPicName_3) ) {
        alert("至少上传1张应用截图！");
        return false;
    }
    return true;
}


// 预览
function imgPreview(fileDom, id){
    //判断是否支持FileReader
    if (window.FileReader) {
        var reader = new FileReader();
    } else {
        alert("您的设备不支持图片预览功能，如需该功能请升级您的设备！");
    }

    //获取文件
    var file = fileDom.files[0];
    var imageType = /^image\//;
    //是否是图片
    if (!imageType.test(file.type)) {
        alert("请选择图片！");
        return;
    }
    //读取完成
    reader.onload = function(e) {
        //获取图片dom
        var img = document.getElementById(id);
        //图片路径设置为读取的图片
        img.src = e.target.result;
    };
    reader.readAsDataURL(file);
}
