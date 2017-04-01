package com.weixin.pay.service;

import com.weixin.pay.domain.generateOrder.OrderData;

/**
 * Created by luc on 17/1/16.
 */
public interface NotifyService {
    //订单完成通知ucoupon后台,执行发送模板消息逻辑
    void notifyUcouponOnFinished(String openid, String out_trade_no, String time_end, double amount_fee, String mobile, String stationId, OrderData orderData);
}
