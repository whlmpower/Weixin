<%--
  Created by IntelliJ IDEA.
  User: luc
  Date: 16/7/19
  Time: 下午6:49
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no"/>
    <title>init</title>
    <script type="text/javascript" src="../jquery-2.2.1.min.js"></script>
</head>
<body>
获取用户openid
<p class="oauth"></p>
<div style="margin-top: 40px">
<form action="paytest.jsp">
    <input class="openid" type="hidden" name="openid">
    <input type="submit" value="confirm">
</form>
</div>
</body>
<script>
    var code = '<%=request.getParameter("code") %>';
    var state = '<%=request.getParameter("state") %>';
    if (code != "null") {
        var url = "/Weixin/userinfo/getopenid.do";
        $.getJSON(url, {"code": code, "state": state}, function (data) {
            var openid = data.openid;
            $(".oauth").text(openid);
            $(".openid").attr("value", openid);
        });
    }
    $.post("/Weixin/pay/testconn.do", {param: "test"});

</script>
</html>
