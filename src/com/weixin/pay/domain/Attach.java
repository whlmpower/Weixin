package com.weixin.pay.domain;

import com.alibaba.fastjson.annotation.JSONType;

/**
 * Created by luc on 16/7/23.
 */
@JSONType(orders = {"tel", "id","name", "car", "ticket", "amount", "coupon", "umoney"})
public class Attach {
    private String tel;
    private String car;
    private String id;
    private String ticket;
    private String amount;
    private String coupon;
    private String umoney;

    public Attach(String car, String id, String ticket, String tel) {
        this.car = car;
        this.id = id;
        this.ticket = ticket;
        this.tel = tel;
    }

    public Attach() {
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCoupon() {
        return coupon;
    }

    public void setCoupon(String coupon) {
        this.coupon = coupon;
    }

    public String getUmoney() {
        return umoney;
    }

    public void setUmoney(String umoney) {
        this.umoney = umoney;
    }

    @Override
    public String toString() {
        return "Attach{" +
                "amount='" + amount + '\'' +
                ", tel='" + tel + '\'' +
                ", car='" + car + '\'' +
                ", id='" + id + '\'' +
                ", ticket='" + ticket + '\'' +
                ", coupon='" + coupon + '\'' +
                ", umoney='" + umoney + '\'' +
                '}';
    }
}
