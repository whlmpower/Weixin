package com.weixin.pay.controller;

import java.io.IOException;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import com.alibaba.fastjson.JSON;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.DateUtils;
import com.weixin.pay.service.ModelMsg;
import com.weixin.pay.service.OrderGenerate;
import com.weixin.pay.service.WxPayService;
import org.jdom.JDOMException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("pay")
public class Payment {

    @Autowired
    private OrderGenerate orderGenerate;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private ModelMsg modelMsg;

    @Autowired
    private TransationDao transationDao;

    @RequestMapping(value = "generate_order", method = RequestMethod.POST)
    @ResponseBody
    String generateOrder(@RequestBody String data, HttpServletRequest request) throws IOException, JDOMException {
        data = new String(data.getBytes("iso8859-1"), "utf-8");
        data = URLDecoder.decode(data, "utf-8");
        int index = data.indexOf("=");
        if (index != -1)
            data = data.substring(0, data.indexOf("="));

        OrderData orderData = JSON.parseObject(data, OrderData.class);

        String out_trade_no = orderGenerate.getOutTradeNoFromUcoupon(orderData);

        String spbill_ip = request.getRemoteAddr();
        System.out.println("从页面获取数据 [" + out_trade_no + "] " + ": " + orderData + "\n");

        int payReal = Integer.parseInt(orderData.getFee());
        System.out.println("实际支付: " + payReal);
        /**
         * 实际支付为0,直接通知后台逻辑
         */
        if (payReal <= 0) {
            System.out.println("实际支付为0,不需要微信支付");

            /**
             * 通知ucoupon后台,出票成功或者失败 模板消息
             * 最好复用实际支付大于0时候，模板消息业务逻辑
             * 但这两种情况，使用不同的REST URL，故可能导致后台出现隐藏性bug
             * 暂时分开写，做修改的时候，记得同步修改这两个方法
             *
             * ！！！！
             * 其实就不应该做出实际支付为0这种业务逻辑
             * 实际支付为0，等于避开了微信支付
             * ！！！！
             */
            wxPayService.sendWxOrderByFree(out_trade_no, orderData);

            /**
             * 支付成功模板消息
             */

            String time_end = DateUtils.changeDateFormatWithTimeEnd(orderData.getCreateTime());
            System.out.println(time_end);
            String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
            time_end = time_end.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
            /**
             * 暂不发送支付成功模板消息
             */
//            NumberFormat CHN = NumberFormat.getCurrencyInstance(Locale.CHINA);
//
//            double amount_fee = Double.parseDouble(orderData.getAmount());
//
//            Map<String, String> msgs = new HashMap<>();
//            msgs.put("openid", orderData.getOpenid());
//            msgs.put("first", "您好,您已支付成功\n");
//            msgs.put("keyword1", out_trade_no);
//            msgs.put("keyword3", orderData.getStation_name());
//            msgs.put("keyword2", CHN.format(amount_fee));
//            msgs.put("keyword4", time_end);
//            msgs.put("remark", "\n感谢您的使用");
//
//            String notify = modelMsg.sendPaySuccessMsg(msgs);
//            System.out.println("模板消息返回状态: " + notify);


            int row2Coupon = transationDao.recordTransation2Ucoupon(out_trade_no, orderData, time_end);
            System.out.println("订单 " + out_trade_no + " 记录 to Ucoupon status " + row2Coupon);
            int row2my = transationDao.recordTransation(out_trade_no, orderData, time_end);
            System.out.println("订单 " + out_trade_no + " 记录 to my status " + row2my);

            Map<String, String> params = new HashMap<>();
            params.put("code", "0");

            return JSON.toJSONString(params);

        }
        /**
         * 实际支付大于0,走微信支付逻辑
         */
        else {
            SortedMap<Object, Object> wxpayParams = orderGenerate.createUnifieldOrderWithCache(out_trade_no, spbill_ip, orderData);
            System.out.println(wxpayParams.toString());
            return JSON.toJSONString(wxpayParams);
        }
    }

    @RequestMapping(value = "notify", method = RequestMethod.POST)
    @ResponseBody
    String getNotifyData(@RequestBody String data) {
        System.out.println(data);
        Map<String, String> map = new HashMap<>();
        map.put("status", "ok");
        return JSON.toJSONString(map);
    }
}
