package com.spring.service.cache;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.spring.service.cache.model.JsApiTicket;
import com.weixin.pay.util.CommonUtils;
import com.weixin.pay.util.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by luc on 16/6/23.
 */
@Service("jsapi_ticket__cache")
public class JsApiTicketCache {

    private static class JsApiTicketHolder{
        private static JsApiTicket jsApiTicket = new JsApiTicket();
    }

    @Autowired
    private AccessTokenCache accessTokenCache;

    private static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    private String getJsApiTicket(){
        String access_token = accessTokenCache.getAccessTokenByCache();
        String url = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
        JSONObject object = JSON.parseObject(CommonUtils.httpRequestByGet(url));
        String ticket = object.getString("ticket");
        return ticket;
    }

    public String getJsApiTicketByCache(){
        long now = System.currentTimeMillis() / 1000;
        long expire = JsApiTicketHolder.jsApiTicket.getExpireTime();
        String ticket = new String();
        if(now < expire){
            ticket = JsApiTicketHolder.jsApiTicket.getTicket();
            System.out.println("使用缓存JsApiTicket");
        }
        else {
            ticket = getJsApiTicket();
            System.out.println("重新获取JsApiTicket");
            expire = now + 7000;
            JsApiTicketHolder.jsApiTicket.setTicket(ticket);
            JsApiTicketHolder.jsApiTicket.setCreateTime(now);
            JsApiTicketHolder.jsApiTicket.setExpireTime(expire);
        }
        return ticket;
    }
}
