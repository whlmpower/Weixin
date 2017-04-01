package com.weixin.pay.domain.modelMsg;

import java.util.List;

/**
 * Created by luc on 17/1/6.
 */
public class ModelMsgReqSuccess {
    private int code;
    private String msg;
    private int msg_num;
    private int msg_success_num;
    private int msg_failure_num;
    private List<ModelMsgInfo> msg_detail;

    public ModelMsgReqSuccess() {
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<ModelMsgInfo> getMsg_detail() {
        return msg_detail;
    }

    public void setMsg_detail(List<ModelMsgInfo> msg_detail) {
        this.msg_detail = msg_detail;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getMsg_failure_num() {
        return msg_failure_num;
    }

    public void setMsg_failure_num(int msg_failure_num) {
        this.msg_failure_num = msg_failure_num;
    }

    public int getMsg_num() {
        return msg_num;
    }

    public void setMsg_num(int msg_num) {
        this.msg_num = msg_num;
    }

    public int getMsg_success_num() {
        return msg_success_num;
    }

    public void setMsg_success_num(int msg_success_num) {
        this.msg_success_num = msg_success_num;
    }
}
