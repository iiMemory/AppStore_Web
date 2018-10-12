<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="css/index.css" media="screen" type="text/css" />
</head>
<body>
<div id="navigation">
    <button type="button" id="login" onclick="login()">登录</button>
</div>

<div id="content">
    <table id="table_app" cellspacing="100">
    <c:forEach var="i" begin="1" end="3" step="1">
        <tr><td class="item_app"></td><td class="item_app"></td><td class="item_app"></td><td class="item_app"></td></tr>
    </c:forEach>
    </table>
</div>

</body>
</html>

