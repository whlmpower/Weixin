package com.weixin.pay;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.common.base.Joiner;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.OrderGenerate;
import com.weixin.pay.service.WxPayService;
import com.weixin.pay.util.CommonUtils;
import com.weixin.pay.util.HttpUtils;
import com.weixin.pay.util.PayUtils;
import com.weixin.util.DeveloperId;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang.CharSet;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.*;

/**
 * Created by luc on 16/7/16.
 */
public class PayTest {
    public static void main(String[] args) throws UnsupportedEncodingException {
//        String url = "http://app1.u-coupon.cn:8000/weixin/distance.php?lat=39.918743&lng=116.499014";
//        for (int i = 0; i < 100; i++) {
//            System.out.println(HttpUtils.doGet(url));
//        }

//        RestTemplate restTemplate = new RestTemplate();
//        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
//        String url = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//
//        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
//        parameters.put("appid", DeveloperId.APPID);
//        parameters.put("mch_id", DeveloperId.MCH_ID);
//        parameters.put("device_info", "WEB");
//        parameters.put("nonce_str", PayUtils.create_nonce_str());
//        parameters.put("body", "优加油");
//        parameters.put("attach", "a");
//        parameters.put("out_trade_no", "20160806124537503771");
//        parameters.put("total_fee", "1");
//        parameters.put("spbill_create_ip", "0.0.0.0");
//        parameters.put("notify_url", "http://wxoa.u-coupon.cn/Weixin/wxpay/notify.do");
//        parameters.put("trade_type", "JSAPI");
//        parameters.put("openid", "oX0_Pwx8vnPffOKxs7WdVJBgj5Ws");
//        String sign = PayUtils.createSign("utf-8", parameters);
//        parameters.put("sign", sign);
//
//        String requestXML = PayUtils.getRequestXml(parameters);
//
//        String result = restTemplate.postForEntity(url, requestXML, String.class).getBody();
//
//        System.out.println(new String(result.getBytes("ISO-8859-1"), "utf-8"));
//
//        System.out.println(HttpUtils.doPost(url,requestXML));

//        for(int i = 0; i < 10; i++){
//            System.out.println(HttpUtils.doPost(url,requestXML));
//        }


        //utf-8编码
//        String city = "北京市";
//        try {
//            byte[] city_b_utf8 = city.getBytes("utf-8");
//            String city_utf8 = new String(city_b_utf8, "utf-8");
//            System.out.println(city_utf8);
//
//            byte[] city_b_iso = city.getBytes("ISO8859-1");
//            String city_iso = new String(city_b_iso, "utf-8");
//            System.out.println(city_iso);
//
//
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

//        String url = "http://restapi.amap.com/v3/geocode/regeo?key=8e42fe48b42e1cd862bb70864016b949&location=LOCATION";
//        String lng = "116.488292";
//        String lat = "39.918952";
//        String locate = Joiner.on(",").join(Arrays.asList(lng,lat));
//        url = url.replace("LOCATION", locate);
//        String result = CommonUtils.httpRequestByGet(url);
//
//        JSONObject jsonObject = JSON.parseObject(result);
//
//        if("1".equals(jsonObject.get("status"))){
//            JSONObject regecode = JSON.parseObject(jsonObject.get("regeocode").toString());
//            JSONObject addressComponent = JSON.parseObject(regecode.get("addressComponent").toString());
//            String city = addressComponent.get("city").toString();
//            System.out.println(city);
//        }

        OrderData orderData = new OrderData();
        System.out.println(orderData);

        WxPayService service = new WxPayService();
        System.out.println(service.changeConponList(orderData.getCoupon_list()));
        /**
         * 订单通知URL
         */
        String url = "http://app1.u-coupon.cn:8000/weixin/wx_order.php?payid=PAYID&timestamp=TIMESTAMP&promotiondata=COUPON&signiture=SIGN";
        String timestamp = CommonUtils.createTimeStampLast5();
        String payId = "2016121813034525155";
        Map<String, String> params = new LinkedHashMap<>();
        params.put("payid", payId);
        params.put("timestamp", timestamp);
        System.out.println(timestamp);
        String sign = CommonUtils.createUcouponSign(params);
        String coupon = "";
        System.out.println(sign);

        if (!coupon.equals(""))
            url = url.replace("PAYID", payId)
                    .replace("TIMESTAMP", timestamp)
                    .replace("COUPON", coupon)
                    .replace("SIGN", sign);
        else
            url = url.replace("PAYID", payId)
                    .replace("TIMESTAMP", timestamp)
                    .replace("COUPON", "empty")
                    .replace("SIGN", sign);

        System.out.println(url);

    }

}
