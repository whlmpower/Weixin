<%--
  Created by IntelliJ IDEA.
  User: luc
  Date: 16/7/23
  Time: 下午4:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>successful</title>
    <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no"/>
</head>
<body>
    支付成功,正在出票,请点击完成返回
    <br>
    <input style="margin-top: 50px" value="完成" type="button" onclick="WeixinJSBridge.call('closeWindow');">
</body>
</html>
