package com.weixin.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baidu.util.GetCityByLocate;
import com.google.common.base.Joiner;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.StationList;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.domain.generateOrder.OrderDataNotify;
import com.weixin.pay.util.CommonUtils;
import com.weixin.pay.util.HttpUtils;
import com.weixin.util.DeveloperId;
import org.apache.commons.collections.buffer.CircularFifoBuffer;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by luc on 16/7/23.
 */
@Service
public class WxPayService {

    private static final String GET_CITY_GMAP = "http://restapi.amap.com/v3/ip?ip=IP&key=2c20526583b5682a10f82924193d30e0";

    private static final String GET_CITYCODE_GMAP = "http://restapi.amap.com/v3/geocode/regeo?key=GMAP&location=LOCATE";

    private static final String TRANS_COORID = "http://restapi.amap.com/v3/assistant/coordinate/convert?locations=LNG,LAT&coordsys=gps&output=json&key=GMAP";

    private static final String STATION_INFO = "http://app1.u-coupon.cn:8000/new_wx/distance.php?lat=LAT&lng=LNG&citycode=CITYCODE";

    private static final String STATION_INFO_BAIDU = "http://app1.u-coupon.cn:8000/new_wx/distance.php?lat=LAT&lng=LNG&city=CITY";

    private static final String AUTHCODE = "http://app1.u-coupon.cn:8000/new_wx/authcode.php?mobile=MOBILE&timestamp=TIMESTAMP&signiture=SIGN";

    private static final String VALIDATE = "http://app1.u-coupon.cn:8000/new_wx/validate.php?mobile=MOBILE&authcode=CODE&station_id=STATION&timestamp=TIMESTAMP&signiture=SIGN";

    private static final String WX_ORDER = "http://app1.u-coupon.cn:8000/new_wx/wx_order.php?payid=PAYID&timestamp=TIMESTAMP&promotiondata=DATA&signiture=SIGN";

    private static final String NOTIFY_UCOUPON_FREE = "http://app1.u-coupon.cn:8000/new_wx/stationdata.php?username=MOBILE&price=AMOUNT&umoney=UMONEY&stationid=STATION&carnumber=LICENSE&couponid=COUPON&invoice=TICKET&ordernum=TRADENO";

    private static final String FETCH_STATION_NAME = "http://app1.u-coupon.cn:8000/new_wx/query_station_name.php?station_id=STATIONID";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private ModelMsg modelMsg;

    @Autowired
    private TransationDao transationDao;

    private String getCityNameByReq(String ip) {
        String city = null;
        String url = GET_CITY_GMAP.replace("IP", ip);
        String result = CommonUtils.httpRequestByGet(url);
        JSONObject data = JSON.parseObject(result);
        if (data.getString("status").equals("1"))
            city = data.getString("city");
        else {
            city = "null";
            System.out.println("获取城市名失败,将城市名置为null");
        }
        return city;
    }

    /**
     * 根据坐标获得citycode
     * 接口调用次数 单个key支持400万次/天，6万次/分钟调用
     * <p>
     * 如果发生异常 citycode为 0000
     *
     * @return
     */
    private String getCityCodeByLocate(String lng, String lat) {
        String citycode = "0000";
        String locate = Joiner.on(",").join(lng, lat);
        String url = GET_CITYCODE_GMAP.replace("GMAP", DeveloperId.GMAP_KEY).replace("LOCATE", locate);
        System.out.println(url);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result);
        JSONObject jsonObject = JSON.parseObject(result);
        if ("1".equals(jsonObject.get("status"))) {
            JSONObject regecode = JSON.parseObject(jsonObject.get("regeocode").toString());
            JSONObject addressComponent = JSON.parseObject(regecode.get("addressComponent").toString());
            citycode = addressComponent.get("citycode").toString();
        }
        return citycode;
    }


    /**
     * 输入按经纬度 输出顺序也是经纬度 lng,lat
     *
     * @param lng
     * @param lat
     * @return
     */
    private String[] gps2Gmap(String lng, String lat) {
        String[] location = new String[2];
        String url = TRANS_COORID.replace("LNG", lng).replace("LAT", lat).replace("GMAP", DeveloperId.GMAP_KEY_MY);
        String result = CommonUtils.httpRequestByGet(url);
        JSONObject data = JSON.parseObject(result);
        if (data.getString("status").equals("1")) {
            String locationStr = data.getString("locations");
            String[] locationSpilt = locationStr.split(",");
            location[0] = locationSpilt[0];
            location[1] = locationSpilt[1];
        } else {
            location[0] = "0";
            location[1] = "1";
            System.out.println("gps转换高德坐标失败,将坐标置为0,0");
        }
        return location;
    }

    /**
     * 通过坐标返回加油站
     * 使用高德地图api
     *
     * @param lng
     * @param lat
     * @return
     */
    public String getStationList(String lng, String lat) throws UnsupportedEncodingException {

        long start = System.currentTimeMillis();
        //获得定位城市区号
        String citycode = getCityCodeByLocate(lng, lat);
        System.out.println("citycode: " + citycode);
        //转换成高德坐标系
        String[] location = gps2Gmap(lng, lat);
        System.out.println("amap location: " + location[0] + "," + location[1]);
        String url = STATION_INFO.replace("LAT", location[1]).replace("LNG", location[0]).replace("CITYCODE", citycode);

        System.out.println("get station: " + url);

        String result = CommonUtils.httpRequestByGet(url);
        long end = System.currentTimeMillis();
        System.out.println("[1]获得加油站列表数据用时: " + (end - start));
        return result;
    }

    /**
     * 通过坐标返回加油站列表
     * 使用百度地图api
     *
     * @param lng
     * @param lat
     * @return
     */
    public String getStationListByBaidu(String lng, String lat, String openid) {
        long start = System.currentTimeMillis();
        String city = GetCityByLocate.getCityByLoc(lng, lat);
        System.out.println("定位城市: " + city);
        String url = STATION_INFO_BAIDU.replace("LAT", lat).replace("LNG", lng).replace("CITY", city);
        System.out.println("station_list url: " + url);
        String result = restTemplate.getForObject(url, String.class);
        StationList stationList = JSON.parseObject(result, StationList.class);

        System.out.println("记录定位结果status: " + transationDao.recordLocateLog(lng, lat, openid, stationList));

        System.out.println("[" + openid + "]: " + result);
        long end = System.currentTimeMillis();
        System.out.println("[1]获得加油站列表数据用时: " + (end - start));
        return result;
    }

    /**
     * 发送验证码。可以拿到发送的验证码,前端可以先行验证
     *
     * @param mobile
     * @return
     */
    public String sendAuthCode(String mobile) {
        String timestamp = CommonUtils.createTimeStampLast5();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("mobile", mobile);
        params.put("timestamp", timestamp);
        String sign = CommonUtils.createUcouponSign(params);
        String url = AUTHCODE.replace("MOBILE", mobile).replace("TIMESTAMP", timestamp).replace("SIGN", sign);
        System.out.println(url);
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject(url, String.class);
        long end = System.currentTimeMillis();
        System.out.println("[2]发送验证码用时: " + (end - start));
        return result;
    }

    /**
     * 验证验证码
     *
     * @param mobile
     * @param authCode
     * @param station
     * @return
     */
    public String validateCode(String mobile, String authCode, String station) {
        String timestamp = CommonUtils.createTimeStampLast5();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("username", mobile);
        params.put("authcode", authCode);
        params.put("station_id", station);
        params.put("timestamp", timestamp);
        String sign = CommonUtils.createUcouponSign(params);
        String url = VALIDATE.replace("MOBILE", mobile)
                .replace("CODE", authCode)
                .replace("STATION", station)
                .replace("TIMESTAMP", timestamp)
                .replace("SIGN", sign);
        System.out.println(url);
        long start = System.currentTimeMillis();
        String result = restTemplate.getForObject(url, String.class);
        long end = System.currentTimeMillis();
        System.out.println("[3]验证验证码用时: " + (end - start));
        return result;
    }

    /**
     * 支付成功提交订单至后台
     *
     * @param out_trade_no 内部订单号
     * @return
     */
    public String sendWxOrder(String out_trade_no, OrderData orderData) {

        long start = System.currentTimeMillis();
        String timestamp = CommonUtils.createTimeStampLast5();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("payid", out_trade_no);
        params.put("timestamp", timestamp);
        String sign = CommonUtils.createUcouponSign(params);

        /**
         * 如果没有使用优惠券,优惠券字段 填empty
         * 如果orderData缓存发生异常 填error
         */
        String list = changeConponList(orderData.getCoupon_list());

        String url = WX_ORDER.replace("PAYID", out_trade_no)
                .replace("TIMESTAMP", timestamp)
                .replace("SIGN", sign)
                .replace("DATA", list);

        System.out.println("通知ucoupon: " + url);
        int row = transationDao.recordUcouponNotify(out_trade_no, orderData.getTel(), url);
        System.out.println("订单 " + out_trade_no + " 记录url status: " + row);
        String result = restTemplate.getForObject(url, String.class);
        System.out.println("ucoupon msg: " + result);

        long end = System.currentTimeMillis();
        System.out.println("[4]通知ucoupon用时: " + (end - start));
        return result;
    }

    /**
     * 实际支付为0,直接通知ucoupon后台
     * 发送出票成功与否 模板消息
     * 实现异步调用
     * 不能使用NotifyService，由于实际支付为0与大于0，通知URL不同
     * @param out_trade_no
     * @param orderData
     * @return
     */
    @Async
    public void sendWxOrderByFree(String out_trade_no, OrderData orderData) {

        String list = changeConponList(orderData.getCoupon_list());

        String url = NOTIFY_UCOUPON_FREE.replace("MOBILE", orderData.getTel())
                .replace("AMOUNT", orderData.getAmount())
                .replace("UMONEY", orderData.getUmoney_discount())
                .replace("STATION", orderData.getStation_id())
                .replace("LICENSE", orderData.getLicense())
                .replace("COUPON", list)
                .replace("TICKET", orderData.getTicket())
                .replace("TRADENO", out_trade_no);
        System.out.println(url);

        System.out.println("通知ucoupon: " + url);
        int row = transationDao.recordUcouponNotify(out_trade_no, orderData.getTel(), url);
        System.out.println("订单 " + out_trade_no + " 记录url status: " + row);

        String result = restTemplate.getForObject(url, String.class);
        System.out.println(result + "\n");

        JSONObject resultJsonObj = JSON.parseObject(result);
        String code = resultJsonObj.getString("code");

        DecimalFormat df = new DecimalFormat("0.00");
        NumberFormat CHN = NumberFormat.getCurrencyInstance(Locale.CHINA);

        /**
         * code 1 出票成功
         * code 0 出票失败
         */
        if (code.equals("1")) {
            System.out.println("~~~~~出票成功~~~~");

            String passCode = resultJsonObj.getString("pass");
            String stationName = resultJsonObj.getString("stationname");
            String ticket = orderData.getTicket();

            String fee = df.format(Double.parseDouble(orderData.getAmount()));
            fee = CHN.format(Double.parseDouble(fee));
            String umoney = "0";
            umoney = CHN.format(Double.parseDouble(umoney));

            //出票成功定制消息
            Map<String, String> ticket_success_msgs = new HashMap<>();
            ticket_success_msgs.put("openid", orderData.getOpenid());
            ticket_success_msgs.put("first", "您好,出票成功,请前往加油\n");
            ticket_success_msgs.put("keyword1", out_trade_no);
            ticket_success_msgs.put("keyword2", stationName);
            ticket_success_msgs.put("keyword3", fee);
            ticket_success_msgs.put("keyword4", passCode);

            StringBuffer remarkBuf = new StringBuffer();
            if (ticket.equals("1")) {
                remarkBuf.append("发票: 开具\n");
            } else {
                remarkBuf.append("发票: 不开具\n");
            }
            remarkBuf.append("\n本次加油返还U币: " + umoney + " \n\n请点击“获取APP”安装优加油APP，查看更多优惠券和返利");

            ticket_success_msgs.put("remark", remarkBuf.toString());

            String ticket_success_notify = modelMsg.sendTicketSuccessMsg(ticket_success_msgs);
            System.out.println("模板消息返回状态: " + ticket_success_notify);
            System.out.println(out_trade_no + " finished");

        } else {
            System.out.println("~~~~出票失败~~~~");

//            String stationName = resultJsonObj.getString("stationname");
//
//            String fee = df.format(Double.parseDouble(orderData.getAmount()));
//            fee = CHN.format(Double.parseDouble(fee));
//
//            //发送出票失败模板消息
//            Map<String, String> ticket_failure_msgs = new HashMap<>();
//            ticket_failure_msgs.put("openid", orderData.getOpenid());
//            ticket_failure_msgs.put("first", "您好,凭证出票失败\n");
//            ticket_failure_msgs.put("keyword1", out_trade_no);
//            ticket_failure_msgs.put("keyword2", stationName);
//            ticket_failure_msgs.put("keyword3", fee);
//            ticket_failure_msgs.put("remark", "\n优加油对于不便表示歉意,支付金额将在1至2个工作日内原路退回您的账户");
//
//            String ticket_failure_notify = modelMsg.sendTicketFailureMsg(ticket_failure_msgs);
//            System.out.println("模板消息返回状态: " + ticket_failure_notify);
//            System.out.println(out_trade_no + " finished");

            System.out.println("订单 " + out_trade_no + "异常");
            System.out.println("\n" + out_trade_no + " finished");
        }

    }

    public String changeConponList(List<String> couponList) {
        StringBuffer buffer = new StringBuffer();
        if (couponList == null)
            return "error";
        if (couponList.isEmpty())
            return "empty";
        for (String s : couponList) {
            buffer.append(s + ",");
        }
        return StringUtils.substringBeforeLast(buffer.toString(), ",");
    }

    /**
     * 通过station_id获取station_name
     * @param station_id
     * @return
     */
    public String fetchStationNameById(String station_id){
        String url = FETCH_STATION_NAME.replace("STATIONID", station_id);
        String station_name = new String();
        String result = new String();
        try {
            result = restTemplate.getForObject(url, String.class);
            JSONObject jsonObject = JSON.parseObject(result);
            int code = jsonObject.getInteger("code");
            if(code == 1)
                station_name = jsonObject.getString("stationname");
            else
                station_name = "NA";
        }catch (Exception e){
            e.printStackTrace();
            System.out.println("获取油站名字异常");
            station_name = "NA";
        }
        return station_name;
    }

}
