<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no"/>
    <style type="text/css">
        body, html, #allmap {
            width: 100%;
            height: 100%;
            overflow: hidden;
            margin: 0;
            font-family: "微软雅黑";
        }
    </style>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=raLVAEr8zl02CFnroyu3C7Bc"></script>
    <script type="text/javascript" src="../jquery-2.2.1.min.js"></script>
    <title>油站地理位置</title>
</head>

<body>
<div id="allmap" style="z-index: 0; position: absolute; left: 0px; right: 0px"></div>
<input class="locate" type="image" src="../image/locate.png"
       style="position: absolute; bottom: 6%; left: 4%; height: 8%">
<a href="list.jsp"><img src="../image/chg2list.png" style="position: absolute; bottom: 14.5%; left: 4%; height: 8%"></a>
</body>

<script type="text/javascript">
    var map = new BMap.Map("allmap");
    <%
    request.setCharacterEncoding("utf-8");
    response.setContentType("text/html;charset=utf-8");
    %>
    var lat = '<%=request.getParameter("lat") %>';
    var lng = '<%=request.getParameter("lng") %>';
    var name = '<%=request.getParameter("name") %>';
    var address = '<%=request.getParameter("address") %>';
    var id = '<%=request.getParameter("id") %>';
    var point = new BMap.Point(lat, lng);
    map.centerAndZoom(point, 15);

    $.getJSON("/Weixin/userinfo/jssdk.do", {url: location.href.split('#')[0]}, function (data) {

        var _appId = data.appId;
        var _signature = data.signature;
        var _timestamp = data.timestamp;
        var _nonceStr = data.noncestr;
        wx.config({
            debug: false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
            appId: data.appId, // 必填，公众号的唯一标识
            timestamp: data.timestamp, // 必填，生成签名的时间戳
            nonceStr: data.noncestr, // 必填，生成签名的随机串
            signature: data.signature,// 必填，签名，见附录1
            jsApiList: ['getLocation'] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
        });

        wx.ready(function () {
            // 在这里调用 API
            wx.getLocation({
                type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                success: function (res) {
                    var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                    var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                    var speed = res.speed; // 速度，以米/每秒计
                    var accuracy = res.accuracy; // 位置精度
                    /* alert(latitude+","+longitude); */

                    var location = new BMap.Point(longitude, latitude);
                    translateCallback = function (data) {
                        if (data.status === 0) {
                            var marker = new BMap.Marker(data.points[0]);
                            map.addOverlay(marker);
                            /* map.panTo(data.points[0]); */
                            /* map.setCenter(data.points[0]); */
                        }
                    }

                    var convertor = new BMap.Convertor();
                    var pointArr = [];
                    pointArr.push(location);
                    convertor.translate(pointArr, 1, 5, translateCallback)
                }

            });

            $(".locate").click(function () {
                wx.getLocation({
                    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
                    success: function (res) {
                        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
                        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
                        var speed = res.speed; // 速度，以米/每秒计
                        var accuracy = res.accuracy; // 位置精度

                        var location = new BMap.Point(longitude, latitude);
                        translateCallback = function (data) {
                            if (data.status === 0) {
                                map.panTo(data.points[0]);
                                /* map.setCenter(data.points[0]); */
                            }
                        }

                        var convertor = new BMap.Convertor();
                        var pointArr = [];
                        pointArr.push(location);
                        convertor.translate(pointArr, 1, 5, translateCallback)
                    }
                });
            });

        });

    });

    /**
     * 标注选中的油站位置
     * @type {BMap.Icon}
     */
    var myIcon = new BMap.Icon("/Weixin/image/ucoupon2.jpg", new BMap.Size(30, 30));
    var marker = new BMap.Marker(point, {icon: myIcon});
    map.addOverlay(marker);
    map.panTo(point);

    var form =
            "<form action='domap.jsp' method='post'>"
            + "<input id='lng' name='lng' type='hidden'>"
            + "<input id='lat' name='lat' type='hidden'>"
            + "<input id='id' name='id' type='hidden'>"
            + "<input id='name' name='name' type='hidden'>"
            + "<input id='address' name='address' type='hidden'>"
            + "</form>";

    var sContent = "<div><span style='font-weight: bold'>地址:</span><span id='info'></span></div>" + form;

    var opts = {
        width: 200,     // 信息窗口宽度
        height: 80,     // 信息窗口高度
        title: name, // 信息窗口标题
    }

    var infoWindow = new BMap.InfoWindow(sContent, opts);

    marker.addEventListener("click", function () {
        map.openInfoWindow(infoWindow, point);

        document.getElementById("info").innerHTML = address;
        document.getElementById("lng").value = lng.toFixed(3);
        document.getElementById("lat").value = lat.toFixed(3);
        document.getElementById("id").value = id;
        document.getElementById("name").value = name;
        document.getElementById("address").value = address;
    });

</script>

<!-- 调整标尺比例 -->
<script>
    var navigate = {
        offset: new BMap.Size(20, 40),
        anchor: BMAP_ANCHOR_BOTTOM_RIGHT,
        type: BMAP_NAVIGATION_CONTROL_SMALL
    };
    map.addControl(new BMap.NavigationControl(navigate));
    var scale = {offset: new BMap.Size(0, 12), anchor: BMAP_ANCHOR_BOTTOM_RIGHT}
    map.addControl(new BMap.ScaleControl(scale));
    map.addControl(new BMap.OverviewMapControl());
    //  map.addControl(new BMap.MapTypeControl());
</script>
</html>