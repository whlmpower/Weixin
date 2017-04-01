package com.weixin.pay.domain;

/**
 * Created by luc on 16/11/16.
 */
public class TimeStampObj {
    private Long timeStamp;

    public TimeStampObj() {
        this.timeStamp = System.currentTimeMillis();
    }

    @Override
    public String toString() {
        return "TimeStampObj{" +
                "timeStamp=" + timeStamp +
                '}';
    }
}
