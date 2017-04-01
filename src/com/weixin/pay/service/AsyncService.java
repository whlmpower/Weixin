package com.weixin.pay.service;

import com.alibaba.fastjson.JSON;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.Attach;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.util.PayUtils;
import com.weixin.util.DeveloperId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.text.SimpleDateFormat;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.Future;

/**
 * Created by luc on 16/11/7.
 */
@Service
public class AsyncService {
    @Autowired
    private RestTemplate restTemplate;

    private static final String UNIFIED_ORDER_URL = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    private static final String NOTIFY_URL = "http://wxoa.u-coupon.cn/new_wx/wxpay/notify.do";

    @Autowired
    private TransationDao transationDao;

    @Async
    public Future<String> fetchUnifiedOrder(String out_trade_no, String spbill_ip, OrderData orderData) {

        Attach attach = new Attach();
        attach.setTel(orderData.getTel());
        attach.setCar(orderData.getLicense());
        attach.setId(orderData.getStation_id());
        attach.setTicket(orderData.getTicket());

        attach.setAmount(orderData.getAmount());
        attach.setCoupon(orderData.getCoupon_discount());
        attach.setUmoney(orderData.getUmoney_discount());
        System.out.println(attach + "\n");

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

        return new AsyncResult<>(restTemplate.postForEntity(UNIFIED_ORDER_URL, requestXML, String.class).getBody());
    }

    @Async
    public Future<String> fetchNow(){
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<>("now");
    }
}
