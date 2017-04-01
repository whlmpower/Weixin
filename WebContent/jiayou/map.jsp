<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" buffer="none"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
    <title>地图</title>
    <style type="text/css">
        html{height:100%}
        body{height:100%;margin:0px;padding:0px}
        #container{height:100%}
    </style>
    <script type="text/javascript" src="http://api.map.baidu.com/api?v=2.0&ak=raLVAEr8zl02CFnroyu3C7Bc">
        //v2.0版本的引用方式：src="http://api.map.baidu.com/api?v=2.0&ak=您的密钥"
        //v1.4版本及以前版本的引用方式：src="http://api.map.baidu.com/api?v=1.4&key=您的密钥&callback=initialize"
    </script>
    <script type="text/javascript" src="../jquery-2.2.1.min.js"></script>
    <script type="text/javascript" src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
	<script type="text/javascript" src="https://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    
</head>

<body>
<div id="container" style="z-index: 0; position: absolute; left: 0px; right: 0px"></div>
<input class="locate" type="image" src="../image/locate.png" style="position: absolute; bottom: 6%; left: 4%; height: 8%">
<a href="list.jsp"><img src="../image/chg2list.png" style="position: absolute; bottom: 14.5%; left: 4%; height: 8%"></a>
<!-- <div id="self">
    <p id="p2"></p>
    <p id="p"></p>
    <p id="name"></p>
</div> -->
</body>

<script type="text/javascript">
	var map = new BMap.Map("container");          // 创建地图实例
	var point = new BMap.Point(116.404, 39.915);  // 创建点坐标
	map.centerAndZoom(point, 15);                 // 初始化地图，设置中心点坐标和地图级别 */
	
	
/* 	$(document).ready(
		function(){
			var geolocation = new BMap.Geolocation();
			geolocation.getCurrentPosition(function(r){
				if(this.getStatus() == BMAP_STATUS_SUCCESS){
					var Icon = new BMap.Icon("/Weixin/image/jiayouzhan.png",new BMap.Size(30,30));
					var mk = new BMap.Marker(r.point,{icon:Icon});
					map.addOverlay(mk);
					map.panTo(r.point); 
					alert('第一次定位：'+r.point.lng+','+r.point.lat); 
				}
				else {
					alert('failed'+this.getStatus());
				}        
			},{enableHighAccuracy: true})
		}		
	) */

</script>

<script type="text/javascript">

	//事件监听，显示当前位置坐标
/* 	map.addEventListener("click",function(e){
		$("#p2").text("点击点坐标:" + e.point.lng + "," + e.point.lat + " ");
	}); */
	
	$.getJSON("/Weixin/userinfo/jssdk.do",{url:location.href.split('#')[0]}, function(data){
		
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
			        translateCallback = function (data){
			            if(data.status === 0) {
			              var marker = new BMap.Marker(data.points[0]);
			              map.addOverlay(marker);
			              map.panTo(data.points[0]);
			              /* map.setCenter(data.points[0]); */
			            }
			        }
			        
			        var convertor = new BMap.Convertor();
			        var pointArr = [];
			        pointArr.push(location);
			        convertor.translate(pointArr, 1, 5, translateCallback)
			      	
			        var url = "/Weixin/station/map.do";
			        $.getJSON(url, {"lng":longitude, "lat":latitude}, function(data){
   			        	$.each(data.station_list,function(index,item){
   			        		var lat = item.latitude;
   			        		var lng = item.longitude;
   			        		var id = item.station_id;
   			        		var name = item.station_name;
   			        		var address = item.address;
   			        		
   			        		var point_target = new BMap.Point(lng, lat);
   			        		addMarker(point_target, id, name, address);
   			        	})
			        })
			        
			    }
			
			});
			    
			$(".locate").click(function(){
				wx.getLocation({
				    type: 'wgs84', // 默认为wgs84的gps坐标，如果要返回直接给openLocation用的火星坐标，可传入'gcj02'
				    success: function (res) {
				        var latitude = res.latitude; // 纬度，浮点数，范围为90 ~ -90
				        var longitude = res.longitude; // 经度，浮点数，范围为180 ~ -180。
				        var speed = res.speed; // 速度，以米/每秒计
				        var accuracy = res.accuracy; // 位置精度
				        
				        var location = new BMap.Point(longitude, latitude);
				        translateCallback = function (data){
				            if(data.status === 0) {
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
	
	
    /* 定位模块 */
/*      $(document).ready(
    	function(){
    	    function core(){
    			var geolocation = new BMap.Geolocation();
    			geolocation.getCurrentPosition(function(r){
    			    if(this.getStatus() == BMAP_STATUS_SUCCESS){
    			        var locate = r.point;
    			        var mk = new BMap.Marker(r.point);
    			        map.addOverlay(mk);
    			        mk.setAnimation(BMAP_ANIMATION_BOUNCE);
    			        map.panTo(r.point);
    			 		alert('第二次定位：'+r.point.lng+','+r.point.lat); 
    			        var url = "/Weixin/station/changeJson.do";
    			        //在传参数前一定要对城市名，进行utf-8转码。
    			        //下面的方法，在后台仍然打印不出中文，但是功能不影响
    			        $.getJSON(url, {"lng":r.point.lng, "lat":r.point.lat}, function(data){
    			        	$.each(data.station_list,function(index,item){
    			        		var lat = item.latitude;
    			        		var lng = item.longitude;
    			        		var id = item.station_id;
    			        		var name = item.name;
    			        		var adress = item.address;
    			        		
    			        		var point_target = new BMap.Point(lat, lng);
    			        		addMarker(point_target, id, name, adress);
    			        	})
    			        })
    			    }
    			    else {
    			        alert('failed'+this.getStatus());
    			        return "**";
    			    }
    			},{enableHighAccuracy: true});
    			
    		}
    		setTimeout(core, 800);
    	}		
    ) */

	
</script>

<!-- 调整标尺比例 -->
<script>
    var navigate = {offset: new BMap.Size(20, 40),anchor: BMAP_ANCHOR_BOTTOM_RIGHT,type: BMAP_NAVIGATION_CONTROL_SMALL};
    map.addControl(new BMap.NavigationControl(navigate));
    var scale = {offset: new BMap.Size(0, 12),anchor: BMAP_ANCHOR_BOTTOM_RIGHT}
    map.addControl(new BMap.ScaleControl(scale));
    map.addControl(new BMap.OverviewMapControl());
    //  map.addControl(new BMap.MapTypeControl());
</script>

<script id="demo">

    function addMarker(point, id, name, address){  // 创建图标对象

        var myIcon = new BMap.Icon("/Weixin/image/ucoupon2.jpg",new BMap.Size(30,30));
		
    	//下面的回调函数是对坐标进行修正
        translateCallback = function (data){
            if(data.status === 0) {
            	
                var marker = new BMap.Marker(data.points[0],{icon:myIcon});
                
                var lng = point.lng;
                var lat = point.lat;

                var form = 
                "<form action='domap.jsp' method='post'>"
                +"<input id='lng' name='lng' type='hidden'>"
                +"<input id='lat' name='lat' type='hidden'>"
                +"<input id='id' name='id' type='hidden'>"
                +"<input id='name' name='name' type='hidden'>"
                +"<input id='address' name='address' type='hidden'>"
                +"</form>";
                
                var sContent = "<div><span style='font-weight: bold'>地址:</span><span id='info'></span></div>"+form;
            	
                var opts = {
            			  width : 200,     // 信息窗口宽度
            			  height: 80,     // 信息窗口高度
            			  title : name , // 信息窗口标题
            			}
            	
                var infoWindow = new BMap.InfoWindow(sContent, opts);
                
            	marker.addEventListener("click", function () {
                    map.openInfoWindow(infoWindow,data.points[0]);
                    
                    document.getElementById("info").innerHTML = address;
                    document.getElementById("lng").value = lng.toFixed(3);
                    document.getElementById("lat").value = lat.toFixed(3);
                    document.getElementById("id").value = id;
                    document.getElementById("name").value = name;
                    document.getElementById("address").value = address;
                });
                map.addOverlay(marker);
            }
        }
    	
        var convertor = new BMap.Convertor();
        var pointArr = [];
        pointArr.push(point);
        //3,5为google坐标转百度坐标
        //1,5为原始坐标转百度坐标
        convertor.translate(pointArr, 1, 5, translateCallback);
    }
</script>
<script type="text/javascript">
/* $(".locate").click(
        function(){
            var geolocation = new BMap.Geolocation();
            geolocation.getCurrentPosition(function(r){
                if(this.getStatus() == BMAP_STATUS_SUCCESS){
                    map.panTo(r.point);
                }
                else {
                    alert('failed'+this.getStatus());
                }
            },{enableHighAccuracy: true})
        }
); */
</script>

<%--获取用户信息,地图页面暂时用不到--%>
<%--<script type="text/javascript">--%>
	<%--$(document).ready(--%>
		<%--function(){--%>
<%--//			function head(){--%>
				<%--var code = '<%=request.getParameter("code") %>';--%>
				<%--var state = '<%=request.getParameter("state") %>';--%>
				<%--var headimgurl = '<%=session.getAttribute("imageurl") %>';--%>
				<%--if(headimgurl != "null"){--%>
					<%--$(".headimg").attr("src",headimgurl);--%>
				<%--}--%>
				<%--if(code != "null"){--%>
					<%--var url = "/Weixin/userinfo/getuserinfo.do";--%>
					<%--$.getJSON(url,{"code":code, "state":state},function(data){--%>
						<%--var nickname = data.nickname;--%>
						<%--var headimgurl = data.headimgurl;--%>
						<%--var openid = data.openid;--%>
						<%--$(".headimg").attr("src",headimgurl);--%>
					<%--});--%>
				<%--}--%>

<%--//			}--%>
<%--//			setTimeout(head, 1000);--%>
		<%--}		--%>
	<%--)--%>
<%--</script>--%>

</html>