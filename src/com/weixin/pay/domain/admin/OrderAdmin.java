package com.weixin.pay.domain.admin;

/**
 * Created by luc on 17/1/10.
 */
public class OrderAdmin {
    private String user;
    private String station_id;
    private String car;
    private String price;
    private String addtime;

    public OrderAdmin(String addtime, String car, String price, String station_id, String user) {
        this.addtime = addtime;
        this.car = car;
        this.price = price;
        this.station_id = station_id;
        this.user = user;
    }

    public OrderAdmin() {
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getCar() {
        return car;
    }

    public void setCar(String car) {
        this.car = car;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getStation_id() {
        return station_id;
    }

    public void setStation_id(String station_id) {
        this.station_id = station_id;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "OrderAdmin{" +
                "addtime='" + addtime + '\'' +
                ", user='" + user + '\'' +
                ", station_id='" + station_id + '\'' +
                ", car='" + car + '\'' +
                ", price='" + price + '\'' +
                '}';
    }
}
