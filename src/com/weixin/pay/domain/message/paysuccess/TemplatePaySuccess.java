package com.weixin.pay.domain.message.paysuccess;

import com.weixin.pay.domain.message.Template;

/**
 * Created by luc on 16/7/25.
 * 支付成功模板消息
 *
 * {{first.DATA}}
 * 订单编号：{{keyword1.DATA}}
 * 消费金额：{{keyword2.DATA}}
 * 消费门店：{{keyword3.DATA}}
 * 消费时间：{{keyword4.DATA}}
 * {{remark.DATA}}
 *
 */

public class TemplatePaySuccess extends Template {

    private PaySuccess data;

    public PaySuccess getData() {
        return data;
    }

    public void setData(PaySuccess data) {
        this.data = data;
    }

    public TemplatePaySuccess(PaySuccess data) {

        this.data = data;

        super.setTemplate_id("jpqkBm8XqnEhsrDzIG69ly2twqznS9O0z-O5YQbfIyo");

    }
}
