package com.weixin.pay.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.NotifyCache;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.*;
import com.weixin.pay.util.XMLUtil;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.parsing.SourceExtractor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luc on 16/7/17.
 */
@Controller
public class PaySuccessNotify {

    @Autowired
    private TransationDao transationDao;

    @Autowired
    private CacheService cacheService;

    @Autowired
    private NotifyService notifyService;

    private static NotifyCache<String, Boolean> notifyCache = new NotifyCache<>(500);

    @RequestMapping("wxpay/notify")
    public void doSuccessNotify(HttpServletRequest request, HttpServletResponse response) throws IOException, JDOMException {

        DecimalFormat df = new DecimalFormat("0.00");
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        System.out.println("~~~~~~~~~~付款成功~~~~~~~~~");

        outSteam.close();
        inStream.close();
        String result = new String(outSteam.toByteArray(), "utf-8");//获取微信调用我们notify_url的返回信息
        Map<Object, Object> map = XMLUtil.doXMLParse(result);
        for (Object keyValue : map.keySet()) {
            System.out.println(keyValue + "=" + map.get(keyValue));
        }

        String openid = (String) map.get("openid");
        String out_trade_no = (String) map.get("out_trade_no");
        String time_end = (String) map.get("time_end");

        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        time_end = time_end.replaceAll(reg, "$1-$2-$3 $4:$5:$6");

        /**
         * 从缓存中拿订单详情
         */
        OrderData orderData = new OrderData();
        orderData = cacheService.handleOrderDateByCache(out_trade_no, orderData);
        System.out.println(out_trade_no + ": from cache: " + orderData);

        //如果通知订单号不在缓存中,说明未通知过
        if (!notifyCache.containsKey(out_trade_no)) {
            notifyCache.put(out_trade_no, false);
        }

        System.out.println("订单: " + out_trade_no + ", 通知satus: " + notifyCache.get(out_trade_no) + " ,map大小: " + notifyCache.size());

        String total_fee_str = (String) map.get("total_fee");
        double total_fee = Double.parseDouble(total_fee_str) / 100;
        total_fee = Double.parseDouble(df.format(total_fee));

        /**
         * 从微信通知中拿attach
         */
        JSONObject attach = JSON.parseObject((String) map.get("attach"));
        String mobile = attach.getString("tel");
        String stationId = attach.getString("id");
        String license = attach.getString("car");
        String amount = attach.getString("amount");

        double amount_fee = Double.parseDouble(amount);
        amount_fee = Double.parseDouble(df.format(amount_fee));

        if (notifyCache.get(out_trade_no) == false) {

            if (map.get("result_code").toString().equalsIgnoreCase("SUCCESS")) {

                response.getWriter().write(XMLUtil.setSuccessStatus("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调action了
                System.out.println("已通知微信后台");

                notifyCache.put(out_trade_no, true);
                /**
                 * 执行通知ucoupon和模板消息逻辑
                 * 为了确保稳定,尽量不使用cache中信息,唯一使用到cache中信息的是优惠券列表
                 * 使用非阻塞模式
                 */
                notifyService.notifyUcouponOnFinished(openid, out_trade_no, time_end, amount_fee, mobile, stationId, orderData);
            }

        } else {
            System.out.println("订单" + out_trade_no + "已重复通知,已做处理");
            response.getWriter().write(XMLUtil.setSuccessStatus("SUCCESS", ""));   //告诉微信服务器，我收到信息了，不要在调用回调action了
            System.out.println("再次通知微信后台");
            notifyCache.put(out_trade_no, true);
        }

    }

    /**
     * 微信回调通知频率 立即/5/30/180/1800/3600秒
     * js得到的通知，等待10秒后，检查一下微信是否回调，如果微信已经回调退出controller
     * 否则，再等待4分钟，如果微信回调还是没到达，则使用js的通知结果处理业务逻辑
     * @param out_trade_no
     * @throws InterruptedException
     */
    @RequestMapping(value = "wxpay/js_notify")
    @ResponseBody
    public void doJsNotify(@RequestParam(value = "out_trade_no", required = true, defaultValue = "0") String out_trade_no) throws InterruptedException {

        if(!"0".equals(out_trade_no)){
            System.out.println("\n" + out_trade_no + " success from js notify");
            Thread.sleep(10 * 1000);
            //已经通知过了
            if(notifyCache.containsKey(out_trade_no)){
                System.out.println(out_trade_no + " [10]秒后check 已经由回调函数通知过\n");
                return;
            }else {
                //等待4分钟,再次查询,如果回调函数未到来,则开始执行业务逻辑
                Thread.sleep(4 * 60 * 1000);
                if(notifyCache.containsKey(out_trade_no)){
                    System.out.println(out_trade_no + " [4]分钟后check 已经由回调函数通知过\n");
                    return;
                }
                else {
                    System.out.println(out_trade_no + " [4]分钟后 回调函数未到来,开始执行业务逻辑\n");
                    notifyCache.put(out_trade_no, true);
                    OrderData orderData = new OrderData();
                    orderData = cacheService.handleOrderDateByCache(out_trade_no, orderData);
                    System.out.println(out_trade_no + " info " + orderData);
                    String openid = orderData.getOpenid();
                    String time_end = DateUtils.changeDateFormatWithTimeEnd(orderData.getCreateTime());
                    DecimalFormat df = new DecimalFormat("0.00");
                    double amount_fee = Double.parseDouble(orderData.getAmount());
                    amount_fee = Double.parseDouble(df.format(amount_fee));
                    String mobile = orderData.getTel();
                    String stationId = orderData.getStation_id();

                    notifyService.notifyUcouponOnFinished(openid, out_trade_no, time_end, amount_fee, mobile, stationId, orderData);
                }
            }
        }
        else {
            System.out.println("\n######error######");
            System.out.println("get " + out_trade_no + " from js notify");
            System.out.println("######error######\n");
        }

    }
}
