package com.weixin.pay.domain;

import java.util.List;

/**
 * Created by luc on 16/12/26.
 */
public class StationList {
    private int code;
    private String detail;
    private List<StationInfo> list;

    public StationList() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public List<StationInfo> getList() {
        return list;
    }

    public void setList(List<StationInfo> list) {
        this.list = list;
    }

    @Override
    public String toString() {
        return "StationList{" +
                "code=" + code +
                ", detail='" + detail + '\'' +
                ", list=" + list +
                '}';
    }
}

