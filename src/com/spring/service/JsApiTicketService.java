package com.spring.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.spring.dao.JsapiTicketDao;
import com.weixin.model.token.JsApiTicket;
import com.weixin.util.DeveloperId;
import com.weixin.util.WeixinUtil;
import net.sf.json.JSONObject;

/*
此类已废弃
使用内存进行缓存
连接数据库的操作可供日后参考
 */
@Service
public class JsApiTicketService {

    @Autowired
    private JsapiTicketDao jsapiTicketDao;

    @Autowired
    private AccessTokenService accessTokenService;


    private static final String JSAPI_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";

    /**
     * 使用缓存的access_token
     * @return
     */
    private String getjsspi_ticket() {
        String access_token = accessTokenService.getAccessToken();
        String url = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
        JSONObject object = WeixinUtil.doGetStr(url);
        String ticket = object.getString("ticket");
        return ticket;
    }

    /**
     * 使用全新的access_token
     * @return
     */
    private String getjsspi_ticket2() {
        String access_token = accessTokenService.getAccessTokenDir();
        String url = JSAPI_TICKET_URL.replace("ACCESS_TOKEN", access_token);
        JSONObject object = WeixinUtil.doGetStr(url);
        String ticket = object.getString("ticket");
        return ticket;
    }

    public String getJsApiTicket() {

        long now = System.currentTimeMillis() / 1000;
        String ticket_db = new String();

        JsApiTicket jsApiTicketDb = jsapiTicketDao.getJsApiTicket_u();

        long expire_time = jsApiTicketDb.getExpire_time();
        ticket_db = jsApiTicketDb.getTicket();

        if (expire_time < now) {
            String ticket = getjsspi_ticket();
            System.out.println("重新获取jsapi_ticket");
            jsapiTicketDao.insertJsApiTicket_u(ticket);
            ticket_db = ticket;
        }
        else{
            System.out.println("使用缓存的jsapi_ticket");
        }

        return ticket_db;
    }

}

