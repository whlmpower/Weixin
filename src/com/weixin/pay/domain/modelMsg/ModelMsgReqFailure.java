package com.weixin.pay.domain.modelMsg;

/**
 * Created by luc on 17/1/6.
 */
public class ModelMsgReqFailure {
    private int code;
    private String msg;

    public ModelMsgReqFailure() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
