package com.weixin.pay.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.pay.domain.Attach;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.util.PayUtils;
import com.weixin.pay.util.XMLUtil;
import com.weixin.util.DeveloperId;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by luc on 16/7/15.
 */
@Service
public class OrderGenerate {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private AsyncService asyncService;

    @Autowired
    private WxPayService wxPayService;

    private static final String NOTIFY_URL = "http://nwxoa.u-coupon.cn/Weixin/wxpay/notify.do";

    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final String OUT_TRADE_NO_URL = "http://app1.u-coupon.cn:8000/new_wx/wx_credentials.php?user=MOBILE&stationid=STATIONID&price=AMOUNT&umoney=UMONEY&cash=FEE&couponid=LIST&couponprice=COUPON&invoice=TICKET&carnumber=LICENSE";

    public String generateOrderNo(Long timeStamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+8"));
        String out_trade_no = "wx";
        out_trade_no += dateFormat.format(timeStamp);
        out_trade_no += String.valueOf(timeStamp).substring(8);
        out_trade_no = out_trade_no.substring(0, 21);
        return out_trade_no;
    }

    public String getOutTradeNoFromUcoupon(OrderData orderData){
        String out_trade_no = new String();
        if(orderData != null){
            String mobile = orderData.getTel();
            String stationId = orderData.getStation_id();
            String amount = orderData.getAmount();
            String umoney = orderData.getUmoney_discount();
            String coupon = orderData.getCoupon_discount();
            String ticket = orderData.getTicket();
            String license = orderData.getLicense();
            List<String> list = orderData.getCoupon_list();
            String fee = orderData.getFee();
            fee = DateUtils.change2yuan(fee);
            String listSend = wxPayService.changeConponList(list);

            String url = OUT_TRADE_NO_URL.replace("MOBILE", mobile)
                    .replace("STATIONID", stationId)
                    .replace("AMOUNT", amount)
                    .replace("UMONEY", umoney)
                    .replace("FEE", fee)
                    .replace("LIST", listSend)
                    .replace("COUPON", coupon)
                    .replace("TICKET", ticket)
                    .replace("LICENSE", license);
            System.out.println(url);
            String jsonStr = restTemplate.getForObject(url, String.class);
            System.out.println("[9]从ucoupon获取订单号: " + jsonStr);
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            int code = jsonObject.getInteger("code");
            if(code == 1){
                out_trade_no = jsonObject.getString("order_num");
            }
        }
        return out_trade_no;
    }

    public SortedMap<Object, Object> createUnifieldOrderWithCache(String out_trade_no, String spbill_ip, OrderData orderData) throws JDOMException, IOException {

        long start = System.currentTimeMillis();
        /**
         * 阻塞请求的方案
         */
        Attach attach = new Attach();
        attach.setTel(orderData.getTel());
        attach.setCar(orderData.getLicense());
        attach.setId(orderData.getStation_id());
        attach.setTicket(orderData.getTicket());

        attach.setAmount(orderData.getAmount());
        attach.setCoupon(orderData.getCoupon_discount());
        attach.setUmoney(orderData.getUmoney_discount());
        System.out.println(out_trade_no + ": " + attach + "\n");

        /*
        统一订单一共有11个参数必填
         */
        SortedMap<Object, Object> parameters = new TreeMap<Object, Object>();
        parameters.put("appid", DeveloperId.APPID);
        parameters.put("mch_id", DeveloperId.MCH_ID);
        parameters.put("device_info", "WEB");
        parameters.put("nonce_str", PayUtils.create_nonce_str());
        parameters.put("body", "优加油");
        parameters.put("attach", JSON.toJSONString(attach));
        parameters.put("out_trade_no", out_trade_no);
        parameters.put("total_fee", orderData.getFee());
        parameters.put("spbill_create_ip", spbill_ip);
        parameters.put("notify_url", NOTIFY_URL);
        parameters.put("trade_type", "JSAPI");
        parameters.put("openid", orderData.getOpenid());
        String sign = PayUtils.createSign("utf-8", parameters);
        parameters.put("sign", sign);

        String requestXML = PayUtils.getRequestXml(parameters);

        String body = restTemplate.postForEntity(UNIFIED_ORDER_URL, requestXML, String.class).getBody();

        /**
         * 非阻塞请求方案
         */
//        String body = new String();
//        String out_trade_no1 = generateOrderNo();
//        Future<String> response1 = asyncService.fetchUnifiedOrder(out_trade_no1, spbill_ip, orderData);
//        try {
//            Thread.sleep(200);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        String out_trade_no2 = generateOrderNo();
//        Future<String> response2 = asyncService.fetchUnifiedOrder(out_trade_no2, spbill_ip, orderData);
//
//        while (true) {
//            if (response1.isDone()) {
//                System.out.println("return 1 request");
//                try {
//                    body = response1.get();
//                    out_trade_no = out_trade_no1;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            if (response2.isDone()) {
//                System.out.println("return 2 request");
//                try {
//                    body = response2.get();
//                    out_trade_no = out_trade_no2;
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                } catch (ExecutionException e) {
//                    e.printStackTrace();
//                }
//                break;
//            }
//            try {
//                Thread.sleep(400);
//                System.out.println("continue");
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//        }


        System.out.println("#####");
        Map<String, String> result = XMLUtil.doXMLParse(body);
        System.out.println("微信统一下单: " + result + "\n");

        //为支付生成js参数
        SortedMap<Object, Object> params = new TreeMap<Object, Object>();
        params.put("appId", DeveloperId.APPID);
        params.put("timeStamp", Long.toString(new Date().getTime()));
        params.put("nonceStr", PayUtils.create_nonce_str());
        params.put("package", "prepay_id=" + result.get("prepay_id"));
        params.put("signType", "MD5");
        String signJs = PayUtils.createSign("utf-8", params);
        params.put("paySign", signJs);
        params.put("code", 1);
        params.put("out_trade_no", out_trade_no);
        long end = System.currentTimeMillis();
        long duration = end - start;
        System.out.println("微信统一下单api调用时间: " + duration);

        orderData.setDuration(duration);
        orderData = cacheService.handleOrderDateByCache(out_trade_no, orderData);
        System.out.println(out_trade_no + ": add cache: " + orderData + "\n");
        return params;
    }
}
