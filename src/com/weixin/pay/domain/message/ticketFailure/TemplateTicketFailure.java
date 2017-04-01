package com.weixin.pay.domain.message.ticketFailure;

import com.weixin.pay.domain.message.Template;

/**
 * Created by luc on 16/8/6.
 *
 * 出票失败模板消息
 *
 * {{first.DATA}}
 * 订单编号：{{keyword1.DATA}}
 * 油站名称：{{keyword2.DATA}}
 * 消费金额：{{keyword3.DATA}}
 * {{remark.DATA}}
 *
 */
public class TemplateTicketFailure extends Template {

    private TicketFailure data;

    public TicketFailure getData() {
        return data;
    }

    public void setData(TicketFailure data) {
        this.data = data;
    }

    public TemplateTicketFailure(TicketFailure data) {

        this.data = data;

        super.setTemplate_id("NQrkF5gdCbuzz_-kMkkSX5NntqdDGhabywobAcEgY1A");

    }
}
