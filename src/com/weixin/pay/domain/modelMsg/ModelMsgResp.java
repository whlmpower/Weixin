package com.weixin.pay.domain.modelMsg;

/**
 * Created by luc on 17/1/6.
 */
public class ModelMsgResp {
    private int errcode;
    private String errmsg;
    private String msgid = "NA";

    public int getErrcode() {
        return errcode;
    }

    public void setErrcode(int errcode) {
        this.errcode = errcode;
    }

    public String getErrmsg() {
        return errmsg;
    }

    public void setErrmsg(String errmsg) {
        this.errmsg = errmsg;
    }

    public String getMsgid() {
        return msgid;
    }

    public void setMsgid(String msgid) {
        this.msgid = msgid;
    }

    public ModelMsgResp() {
    }
}
