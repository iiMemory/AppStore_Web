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
    var file = $('#file').val();
    var packageId = $('#packageId').val();
    var name = $('#name').val();
    var describe = $('#describe').val();
    if (!$.trim(file)) {
        alert("请选择要上传的文件！")
        return false;
    }
    if (!$.trim(packageId)) {
        alert("请输入应用包名！");
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
    return true;
}