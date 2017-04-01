package com.weixin.pay.domain;

/**
 * Created by luc on 17/1/16.
 */
public class BdyzInfo {
    private String openid;
    private String mobile;
    private String auth_code;

    public BdyzInfo(String auth_code, String mobile, String openid) {
        this.auth_code = auth_code;
        this.mobile = mobile;
        this.openid = openid;
    }

    public BdyzInfo() {
    }

    public String getAuth_code() {
        return auth_code;
    }

    public void setAuth_code(String auth_code) {
        this.auth_code = auth_code;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    @Override
    public String toString() {
        return "BdyzInfo{" +
                "auth_code='" + auth_code + '\'' +
                ", openid='" + openid + '\'' +
                ", mobile='" + mobile + '\'' +
                '}';
    }
}
