package com.weixin.pay.domain.message;

/**
 * Created by luc on 16/7/25.
 */
public class DetailMsg {

    private String value;

    private String color;

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public DetailMsg() {
        this.color = "#173177";
    }
}
