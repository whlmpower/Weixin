package com.weixin.pay.domain.message;

/**
 * Created by luc on 16/12/19.
 */
public class RemarkMsg {
    private String value;
    private String color;

    public RemarkMsg() {
        this.color = "#FF3030";
    }

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
}
