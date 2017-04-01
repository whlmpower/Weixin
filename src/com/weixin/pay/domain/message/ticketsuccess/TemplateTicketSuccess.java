package com.weixin.pay.domain.message.ticketsuccess;

import com.weixin.pay.domain.message.Template;

/**
 * Created by luc on 16/8/2.
 * 出票成功模板消息
 *
 * {{first.DATA}}
 * 订单编号：{{keyword1.DATA}}
 * 消费金额：{{keyword2.DATA}}
 * 消费门店：{{keyword3.DATA}}
 * 消费时间：{{keyword4.DATA}}
 * {{remark.DATA}}
 *
 */
public class TemplateTicketSuccess extends Template{

    private TicketSuccess data;

    public TicketSuccess getData() {
        return data;
    }

    public void setData(TicketSuccess data) {
        this.data = data;
    }

    public TemplateTicketSuccess(TicketSuccess data) {
        this.data = data;
        super.setUrl("http://www.u-coupon.cn/mobile/download.html");
        super.setTemplate_id("FZhf5e2jvQl-0fTO97694n8G4jai3JO_oex0jzDKSZg");
    }
}
