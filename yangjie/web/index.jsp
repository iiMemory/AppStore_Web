<%@ page contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="css/index.css" media="screen" type="text/css" />
    <script type="text/javascript" src="https://code.jquery.com/jquery-3.3.1.min.js"></script>
    <script type="text/javascript" src="js/index.js"></script>
</head>
<body onload="loadAppList()">
<div id="navigation">
    <button type="button" id="login" onclick="login()">登录</button>
</div>

<div id="content">
    <table id="table_app" cellspacing="100">
        <tbody></tbody>
    <%--<c:forEach var="i" begin="1" end="3" step="1">--%>
        <%--<tr><td class="item_app"></td><td class="item_app"></td><td class="item_app"></td><td class="item_app"></td></tr>--%>
    <%--</c:forEach>--%>
    </table>
</div>

</body>
</html>
