package com.weixin.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.ModelMsg;
import com.weixin.pay.service.NotifyService;
import com.weixin.pay.service.WxPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Created by luc on 17/1/16.
 */
@Service
public class NotifyServiceImpl implements NotifyService {

    @Autowired
    private ModelMsg modelMsg;

    @Autowired
    private WxPayService wxPayService;

    @Autowired
    private TransationDao transationDao;

    @Override
    public void notifyUcouponOnFinished(String openid, String out_trade_no, String time_end, double amount_fee, String mobile, String stationId, OrderData orderData) {

        NumberFormat CHN = NumberFormat.getCurrencyInstance(Locale.CHINA);
        DecimalFormat df = new DecimalFormat("#.00");

        /**
         * 不发送支付成功模板消息
         */
        //发送支付成功模板消息
//        Map<String, String> msgs = new HashMap<>();
//        msgs.put("openid", openid);
//        msgs.put("first", "您好,您已支付成功\n");
//        msgs.put("keyword1", out_trade_no);
//        msgs.put("keyword3", orderData.getStation_name());
//        msgs.put("keyword2", CHN.format(amount_fee));
//        msgs.put("keyword4", time_end);
//        msgs.put("remark", "\n感谢您的使用");
//
//        String notify = modelMsg.sendPaySuccessMsg(msgs);
//        System.out.println("模板消息返回状态: " + notify);

        /**
         * 通知订单消息至ucoupon后台
         */
        String ucouponMsg = wxPayService.sendWxOrder(out_trade_no, orderData);

        String code = new String();
        JSONObject ucouponMsgObj = null;
        try {
            ucouponMsgObj = JSON.parseObject(ucouponMsg);
            code = ucouponMsgObj.getString("code");
        }catch (Exception e){
            code = "-1";
            System.out.println(out_trade_no + "通知ucoupon后台出现异常");
        }

        if ("1".equals(code)) {

            System.out.println("~~~~~~出票成功~~~~~~~~");
            String stationName = ucouponMsgObj.getString("stationname");

            String passCode = ucouponMsgObj.getString("pass");

            String umoney = df.format(Double.parseDouble(ucouponMsgObj.getString("umoney")));
            umoney = CHN.format(Double.parseDouble(umoney));

            String ticket = ucouponMsgObj.getString("ticket");

            //出票成功定制消息
            Map<String, String> ticket_success_msgs = new HashMap<>();
            ticket_success_msgs.put("openid", openid);
            ticket_success_msgs.put("first", "您好,出票成功,请前往加油\n");
            ticket_success_msgs.put("keyword1", out_trade_no);
            ticket_success_msgs.put("keyword2", stationName);
            ticket_success_msgs.put("keyword3", CHN.format(amount_fee));
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
            System.out.println("\n" + out_trade_no + " finished");

        } else {
            System.out.println("出票失败,退款逻辑");

            /**
             * 异常情况,暂时不发模板消息
             */
//            String stationName = wxPayService.fetchStationNameById(stationId);
//
//            //发送出票失败模板消息
//            Map<String, String> ticket_failure_msgs = new HashMap<>();
//            ticket_failure_msgs.put("openid", openid);
//            ticket_failure_msgs.put("first", "您好,凭证出票失败\n");
//            ticket_failure_msgs.put("keyword1", out_trade_no);
//            ticket_failure_msgs.put("keyword2", stationName);
//            ticket_failure_msgs.put("keyword3", CHN.format(amount_fee));
//            ticket_failure_msgs.put("remark", "\n优加油对于不便表示歉意,支付金额将在1至2个工作日内原路退回您的账户");
//
//            String ticket_failure_notify = modelMsg.sendTicketFailureMsg(ticket_failure_msgs);
//            System.out.println("模板消息返回状态: " + ticket_failure_notify);

            System.out.println("\n" + out_trade_no + " finished");
            int rowErrorOrder = transationDao.recordErrorOrder(out_trade_no, amount_fee, mobile);
            System.out.println("订单 " + out_trade_no + "异常 记录 to Ucoupon status " + rowErrorOrder);
        }

        //插入数据库
        int row2Coupon = transationDao.recordTransation2Ucoupon(out_trade_no, orderData, time_end);
        System.out.println("订单 " + out_trade_no + " 记录 to Ucoupon status " + row2Coupon);
        int row2my = transationDao.recordTransation(out_trade_no, orderData, time_end);
        System.out.println("订单 " + out_trade_no + " 记录 to my status " + row2my);
    }
}
