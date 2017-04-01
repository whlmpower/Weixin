package com.weixin.pay.domain;

/**
 * Created by luc on 16/12/26.
 */
public class StationInfo {
    private String station_name;
    private String station_id;
    private double min;

    public double getMin() {
        return min;
    }

    public void setMin(double min) {
        this.min = min;
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

    @Override
    public String toString() {
        return "StationInfo{" +
                "min='" + min + '\'' +
                ", station_name='" + station_name + '\'' +
                ", station_id='" + station_id + '\'' +
                '}';
    }
}
