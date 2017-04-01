$(document).ready(function(){
    var station_id;
    $(".station_select").click(function(e){
        $.ajax({
            type:"GET",
            url:"http://wxoa.u-coupon.cn/Weixin/util/station.do",
            dataType:"jsonp",
            data:{lng:"116.49298",lat:"39.9175"},
            jsonp:"callback",
            success:function(data){
                if(data.code=="1"){
                    console.log(data);
                    $("#station_show").html(data.list[0].station_name);
                    station_id=data.list[0].station_id;
                    $(".station_list").css("display",'');
                    $("#list").children("li").remove();
                    for(var i=1;i<6;i++){
                        $("#list").append("<li>"+data.list[i].station_name+"</li>");

                    }

                }else{
                    $("#station_show").html("一公里内无可用油站");
                    for(var i=0;i<5;i++){
                        $("#list").append("<li>"+data.list[i].station_name+"</li>");
                    };
                }
                $("li").click(function(e){
                    console.log($(e.target).html());
                    $("#station_show").html($(e.target).html());
                    e.stopPropagation();
                    for(var i=0;i<6;i++){
                        if($(e.target).html()==data.list[i].station_name){
                            station_id=data.list[i].station_id;
                            console.log(station_id);
                            function shut(){
                                $(".station_list").css("display",'none');
                            }
                            setTimeout(shut,100);
                        }
                    }

                });
                $("#station_show").click(function(e){
                    $(".station_list").css("display",'none');
                    e.stopPropagation();
                });


            },
            error:function(jqXHR){
                alert("发生错误:"+jqXHR.status);
            }

        })

    });
    var authcode;
    $("#send").click(function(){
        var tel=$("#telNum").val();
        var reg = /^1[3|4|5|7|8][0-9]{9}$/;
        if(reg.test(tel)){
            $.ajax({
                type:"GET",
                url:"http://wxoa.u-coupon.cn/Weixin/util/send_auth_code.do",
                dataType:"jsonp",
                data:{mobile:tel},
                jsonp:"callback",
                success:function(data){
                    if(data.code=="1"){
                        authcode=data.authcode;
                        alert(data.detail);
                    }else{
                        alset(data.detail);
                    }
                },
                error:function(jqXHR){
                    alert("发生错误:"+jqXHR.status);
                }
            })
        }else{
            alert("手机号填写错误，请重新填写");
            console.log($("#telNum")[0]);
            $("#telNum")[0].focus();

        }
    });
    var tradeNo;
    $("#btn").click(function(){
        var tel=$("#telNum").val();
        var code=$("#codeIn").val();
        $.ajax({
            type:"GET",
            url:"http://wxoa.u-coupon.cn/Weixin/util/validate_code.do",
            dataType:"jsonp",
            data:{mobile:tel,auth_code:code,station:station_id},
            jsonp:"callback",
            success:function(data){
                if(data.code=="1"){
                    alert(data.detail);
                    tradeNo=data.out_trade_no;
                    console.log(tradeNo);
                }else{
                    alert(data.detail);
                }
            },
            error:function(jqXHR){
                alert("发生错误:"+jqXHR.status);
            }
        })
    });

});