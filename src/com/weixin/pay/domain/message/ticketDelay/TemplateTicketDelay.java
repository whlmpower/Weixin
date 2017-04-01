package com.weixin.pay.domain.message.ticketDelay;

import com.weixin.pay.domain.message.Template;

/**
 * Created by luc on 17/1/5.
 * {{first.DATA}}
 * 订单编号：{{keyword1.DATA}}
 * 油站名称：{{keyword2.DATA}}
 * 消费金额：{{keyword3.DATA}}
 * 手机尾号：{{keyword4.DATA}}
 * 手机号：{{keyword5.DATA}}
 * {{remark.DATA}}
 */
public class TemplateTicketDelay extends Template{

    private TicketDelay data;

    public TicketDelay getData() {
        return data;
    }

    public void setData(TicketDelay data) {
        this.data = data;
    }

    public TemplateTicketDelay(TicketDelay data) {
        this.data = data;
        super.setTemplate_id("7H2DevRsOCRvUxfKfZrWi_tSIA-hPps1jFle9lUH-ig");
    }
}
