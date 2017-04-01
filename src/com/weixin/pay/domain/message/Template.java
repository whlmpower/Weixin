package com.weixin.pay.domain.message;

/**
 * Created by luc on 16/8/6.
 */
public class Template {

    private String touser;

    private String template_id;

    private String url;

    private String topcolor;

    public String getTemplate_id() {
        return template_id;
    }

    public void setTemplate_id(String template_id) {
        this.template_id = template_id;
    }

    public String getTopcolor() {
        return topcolor;
    }

    public void setTopcolor(String topcolor) {
        this.topcolor = topcolor;
    }

    public String getTouser() {
        return touser;
    }

    public void setTouser(String touser) {
        this.touser = touser;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Template() {
        this.topcolor = "#FF0000";
    }
}
