package com.weixin.pay.domain.generateOrder;

import com.alibaba.fastjson.annotation.JSONType;

import java.io.Serializable;
import java.util.List;

/**
 * Created by luc on 16/10/16.
 */
@JSONType(orders = {"openid", "createTime", "fee", "amount", "favorable", "duration", "umoney_discount", "coupon_discount", "tel", "license", "station_id", "station_name", "ticket", "coupon_list"})
public class OrderData implements Serializable {

    private String openid;
    private Long createTime;
    private String fee;
    private String tel;
    private String license;
    private String station_id;
    private String station_name;
    private String ticket;
    private String amount;
    private String favorable;
    private String umoney_discount;
    private String coupon_discount;
    private Long duration = 0l;
    private List<String> coupon_list;

    public OrderData(String amount, String coupon_discount, List<String> coupon_list, Long createTime, Long duration, String favorable, String fee, String license, String openid, String station_id, String station_name, String tel, String ticket, String umoney_discount) {
        this.amount = amount;
        this.coupon_discount = coupon_discount;
        this.coupon_list = coupon_list;
        this.createTime = createTime;
        this.duration = duration;
        this.favorable = favorable;
        this.fee = fee;
        this.license = license;
        this.openid = openid;
        this.station_id = station_id;
        this.station_name = station_name;
        this.tel = tel;
        this.ticket = ticket;
        this.umoney_discount = umoney_discount;
    }

    public OrderData() {
    }

    @Override
    public String toString() {
        return "OrderData{" +
                "amount='" + amount + '\'' +
                ", openid='" + openid + '\'' +
                ", createTime=" + createTime +
                ", fee='" + fee + '\'' +
                ", tel='" + tel + '\'' +
                ", license='" + license + '\'' +
                ", station_id='" + station_id + '\'' +
                ", station_name='" + station_name + '\'' +
                ", ticket='" + ticket + '\'' +
                ", favorable='" + favorable + '\'' +
                ", umoney_discount='" + umoney_discount + '\'' +
                ", coupon_discount='" + coupon_discount + '\'' +
                ", duration=" + duration +
                ", coupon_list=" + coupon_list +
                '}';
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getCoupon_discount() {
        return coupon_discount;
    }

    public void setCoupon_discount(String coupon_discount) {
        this.coupon_discount = coupon_discount;
    }

    public List<String> getCoupon_list() {
        return coupon_list;
    }

    public void setCoupon_list(List<String> coupon_list) {
        this.coupon_list = coupon_list;
    }

    public String getFavorable() {
        return favorable;
    }

    public void setFavorable(String favorable) {
        this.favorable = favorable;
    }

    public String getFee() {
        return fee;
    }

    public void setFee(String fee) {
        this.fee = fee;
    }

    public String getLicense() {
        return license;
    }

    public void setLicense(String license) {
        this.license = license;
    }

    public String getTicket() {
        return ticket;
    }

    public void setTicket(String ticket) {
        this.ticket = ticket;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getUmoney_discount() {
        return umoney_discount;
    }

    public void setUmoney_discount(String umoney_discount) {
        this.umoney_discount = umoney_discount;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }
}
