package com.weixin.pay.domain.generateOrder;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;

/**
 * Created by luc on 16/10/16.
 */
@JSONType(orders = {"out_trade_no", "data"})
public class OrderDataNotify implements Serializable{
    private String out_trade_no;
    private OrderData data;

    public OrderData getData() {
        return data;
    }

    public void setData(OrderData data) {
        this.data = data;
    }

    public String getOut_trade_no() {
        return out_trade_no;
    }

    public void setOut_trade_no(String out_trade_no) {
        this.out_trade_no = out_trade_no;
    }

    public OrderDataNotify(OrderData data, String out_trade_no) {
        this.data = data;
        this.out_trade_no = out_trade_no;
    }

    public OrderDataNotify() {
    }
}

