package com.weixin.pay.controller;

import com.alibaba.fastjson.JSON;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.AsyncService;
import com.weixin.pay.service.QRService;
import com.weixin.pay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luc on 16/7/23.
 */
@Controller
@RequestMapping(value = "util")
public class Common {

    private static final String VALIDATE_NEW = "http://app1.u-coupon.cn:8000/new_wx/validate.php?mobile=MOBILE&authcode=CODE&station_id=STATION";

    private static final String DISCOUNT_CONPON = "http://app1.u-coupon.cn:8000/new_wx/promotionlist.php?username=MOBILE";

    private static final String QUERY_STATION = "http://103.235.227.186:8000/new_wx/query_station_name.php?station_id=STATION";
    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private QRService qrService;

    @RequestMapping(value = "station")
    public void getCityByReq(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String lng = request.getParameter("lng");
        String lat = request.getParameter("lat");
        String openid = request.getParameter("openid");
        System.out.println("[" + openid + "] " + "get location: " + lng + "," + lat);

//        String stationList = wxPayService.getStationList(lng, lat);
        String stationList = wxPayService.getStationListByBaidu(lng, lat, openid);

        out.print(stationList);
    }

    @RequestMapping(value = "send_auth_code")
    public void sendAuthCode(@RequestParam("mobile") String mobile,
                             @RequestParam("callback") String callback,
                             HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        System.out.println("get tel: " + mobile);
        String result = wxPayService.sendAuthCode(mobile);
        System.out.println("收到验证码json: " + result);
        out.print(callback + "(" + result + ")");
    }

    @RequestMapping(value = "validate_code")
    public void validateCode(HttpServletRequest request, HttpServletResponse response) throws IOException {
        PrintWriter out = response.getWriter();
        String mobile = request.getParameter("mobile");
        String authcode = request.getParameter("auth_code");
        String station = request.getParameter("station");
        System.out.println("验证,验证码接口收到的数据: " + mobile + "," + authcode + "," + station);
        String result = wxPayService.validateCode(mobile, authcode, station);
        out.print(result);
    }

    @RequestMapping(value = "validate_code_new")
    @ResponseBody
    String validateCodeNew(@RequestParam(value = "mobile", required = false, defaultValue = "0") String mobile,
                           @RequestParam(value = "auth_code", required = false, defaultValue = "0") String authCode,
                           @RequestParam(value = "station", required = false, defaultValue = "0") String station) {

        String url = VALIDATE_NEW.replace("MOBILE", mobile)
                .replace("CODE", authCode)
                .replace("STATION", station);
        System.out.println(url);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result + "\n");
        return result;
    }

    @RequestMapping(value = "discount_coupon")
    @ResponseBody
    String discountCoupon(@RequestParam(value = "mobile", required = false, defaultValue = "0") String moblie) {
        String url = DISCOUNT_CONPON.replace("MOBILE", moblie);
        System.out.println(url);
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject(url, String.class);
        long end = System.currentTimeMillis();
        System.out.println("获取优惠券用时: " + (end - start));
        return result;
    }

    @RequestMapping(value = "query_station_name", produces = "application/json;charset=utf-8")
    @ResponseBody
    String queryForStationNameById(@RequestParam(value = "station_id", required = false, defaultValue = "000") String stationId) {
        return JSON.toJSONString(qrService.queryForStationNameById(stationId));
    }

    @RequestMapping(value = "query_station_name_php")
    @ResponseBody
    String queryForStationNameByIdPhp(@RequestParam(value = "station_id", required = false, defaultValue = "000") String stationId) {
        String url = QUERY_STATION.replace("STATION", stationId);
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject(url, String.class);
        long end = System.currentTimeMillis();
        System.out.println("查询油站名字用时: " + (end - start));
        return result;
    }

}
