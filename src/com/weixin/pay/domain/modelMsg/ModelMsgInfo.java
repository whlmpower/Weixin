package com.weixin.pay.domain.modelMsg;

/**
 * Created by luc on 17/1/6.
 */
public class ModelMsgInfo {
    private String openid;
    private ModelMsgResp data;

    public ModelMsgInfo() {
    }

    public ModelMsgInfo(ModelMsgResp data, String openid) {
        this.data = data;
        this.openid = openid;
    }

    public ModelMsgResp getData() {
        return data;
    }

    public void setData(ModelMsgResp data) {
        this.data = data;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

}
