<%@ page import="java.net.URLDecoder" %><%--
  Created by IntelliJ IDEA.
  User: luc
  Date: 16/7/15
  Time: 上午9:09
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1,user-scalable=no"/>
    <title>pay</title>
    <script type="text/javascript" src="../jquery-2.2.1.min.js"></script>
    <script type="text/javascript"
            src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript"
            src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
获取用户openid
<p class="openid"></p>
<p class="tel">tel: </p>
<p class="license">license: </p>
<p class="fee">fee: </p>
<p class="ticket">ticket: </p>
<p class="trade_no">trade_no: </p>
<p class="station_id">station_id: </p>
<p class="station_name">station_name: </p>
<div class="content">

</div>

<form action="" method="post">
    <input type="button" value="pay" id="paytest">
</form>
</body>
<%
    request.setCharacterEncoding("utf-8");
%>
<script>

    var openid = '<%=request.getParameter("openid") %>';
    var fee = '<%=request.getParameter("fee") %>';
    var trade_no = '<%=request.getParameter("trade_no") %>';

    var tel = '<%=request.getParameter("tel") %>';
    var license = '<%=URLDecoder.decode(request.getParameter("license"), "utf-8") %>';
    var ticket = '<%=request.getParameter("ticket") %>';
    var station_id = '<%=request.getParameter("id") %>';
    var station_name = '<%=URLDecoder.decode(request.getParameter("name"), "utf-8") %>';

    $(".openid").append(openid);
    $(".tel").append(tel);
    $(".license").append(license);
    $(".fee").append(fee);
    $(".ticket").append(ticket);
    $(".trade_no").append(trade_no);
    $(".station_id").append(station_id);
    $(".station_name").append(station_name);

    $.getJSON(
            "../pay/getorderinfo.do",
            {
                openid: openid,
                trade_no: trade_no,
                fee: fee,
                tel: tel,
                license: license,
                ticket: ticket,
                station_id: station_id,
                station_name: station_name
            },
            function (order) {

                var appId = order.appId;
                var timeStamp = order.timeStamp;
                var nonceStr = order.nonceStr;
                var package = order.package;
                var signType = order.signType;
                var paySign = order.paySign;

                var html = "<p>appId: " + appId + "</p>";
                html += "<p>timeStamp: " + timeStamp + "</p>";
                html += "<p>nonceStr: " + nonceStr + "</p>";
                html += "<p>package: " + package + "</p>";
                html += "<p>signType: " + signType + "</p>";
                html += "<p>paySign: " + paySign + "</p>";

                $(".content").html(html);

                $("#paytest").on("click", function () {
                    callpay();
                })
                function callpay() {
                    if (typeof WeixinJSBridge == "undefined") {
                        if (document.addEventListener) {
                            document.addEventListener('WeixinJSBridgeReady', jsApiCall, false);
                        } else if (document.attachEvent) {
                            document.attachEvent('WeixinJSBridgeReady', jsApiCall);
                            document.attachEvent('onWeixinJSBridgeReady', jsApiCall);
                        }
                    } else {
                        jsApiCall();
                    }
                }

                function jsApiCall() {
                    WeixinJSBridge.invoke(
                            'getBrandWCPayRequest', {
                                "appId": appId,     //公众号名称，由商户传入
                                "timeStamp": timeStamp,         //时间戳，自1970年以来的秒数
                                "nonceStr": nonceStr, //随机串
                                "package": package,
                                "signType": signType,         //微信签名方式：
                                "paySign": paySign //微信签名
                            },
                            function (res) {
                                var msg = res.err_msg;
                                if (msg == "get_brand_wcpay_request:ok") {
                                    window.location.href = "success.jsp";
                                } else {
                                    alert("error");
                                }
                            }
                    );
                }

            });
</script>
</html>
