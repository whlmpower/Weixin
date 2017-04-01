package com.weixin.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.spring.service.AccessTokenService;
import com.weixin.pay.domain.admin.OrderAdmin;
import com.weixin.pay.service.AdminService;
import com.weixin.pay.service.DateUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by luc on 17/1/10.
 */
@Service
public class AdminServiceImpl implements AdminService {

    private static String QUERY_URL = "https://app1.u-coupon.cn:448/new_wx/Query_station_log.php?openid=OPENID";

    private static String LINK_URL = "http://app1.u-coupon.cn:8000/new_wx/verify_tg_user.php?number=MOBILE&authcode=CODE&openid=";

    private static String BD_URL = "http://app1.u-coupon.cn:8000/new_wx/BDYZ.php?openid=OPENID";

    private static String USER_INFO_URL = "https://api.weixin.qq.com/cgi-bin/user/info?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessTokenService accessTokenService;

    @Override
    public String queryTodayOrders(String openid, String createTime) {
        StringBuffer buffer = new StringBuffer();
        if (openid != null) {
            long start = System.currentTimeMillis();
            String url = QUERY_URL.replace("OPENID", openid);
            String jsonStr = restTemplate.getForObject(url, String.class);
            System.out.println(jsonStr);
            JSONObject json = JSON.parseObject(jsonStr);
            long end = System.currentTimeMillis();
            System.out.println("[8]获取今日交易信息用时: " + (end - start));
            int code = json.getInteger("code");
            if (code == 1) {
                try {
                    String sum = json.getString("sumprice");
                    String sumStr = DateUtils.change2CNY(sum);
                    int count = json.getInteger("count");
                    String stationName = json.getString("station_name");
                    JSONArray list = (JSONArray) json.get("list");
                    buffer.append(DateUtils.getToday(createTime) + "日 " + stationName + "\n");
                    buffer.append("交易总额: " + sumStr + "\n");
                    buffer.append("交易量: " + count + "\n\n");
                    buffer.append("订单详情:\n");
                    for (int i = 0; i < list.size(); i++) {
                        OrderAdmin order = JSON.parseObject(list.getString(i), OrderAdmin.class);
                        String time = order.getAddtime().split(" ")[1];
                        String mobile = order.getUser().substring(7);
                        buffer.append(time + "  " + DateUtils.change2CNY(order.getPrice()) + "  " + order.getCar() + "\n");
                    }
                    return buffer.toString();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else if(code == 2){
                buffer.append(DateUtils.getToday(createTime) + "日 \n");
                buffer.append("暂时没有交易");
                return buffer.toString();
            }
            else {

            }
        }
        buffer.append("<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3b4316026c47b1a5&redirect_uri=http%3a%2f%2fnwxoa.u-coupon.cn%2fWeixin%2fqr%2fbdyz.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect\">请绑定加油站</a>");
        return buffer.toString();
    }

    @Override
    public String queryBDStation(String openid) {
        StringBuffer res = new StringBuffer();
        if(!StringUtils.isEmpty(openid)){
//            String nickName = fetchNickNameByOpenid(openid);
            long start = System.currentTimeMillis();
            String url = BD_URL.replace("OPENID", openid);
            String jsonStr = restTemplate.getForObject(url, String.class);
            System.out.println(jsonStr);
            long end = System.currentTimeMillis();
            System.out.println("[7]获取用户绑定油站信息用时: " + (end - start));
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            int code = jsonObject.getInteger("code");
            if(code == 1){
                String stationId = jsonObject.getString("stationid");
                String stationName = jsonObject.getString("stationname");
//                res.append("您好, ").append(nickName).append("\n");
                res.append("您所绑定的加油站信息:\n\n");
                res.append(stationId).append("\n");
                res.append(stationName);
            }
            else {
//                res.append("您好, ").append(nickName).append("\n\n");
                res.append("<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3b4316026c47b1a5&redirect_uri=http%3a%2f%2fnwxoa.u-coupon.cn%2fWeixin%2fqr%2fbdyz.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect\">请绑定加油站</a>");
            }
            return res.toString();
        }
        res.append("<a href=\"https://open.weixin.qq.com/connect/oauth2/authorize?appid=wx3b4316026c47b1a5&redirect_uri=http%3a%2f%2fnwxoa.u-coupon.cn%2fWeixin%2fqr%2fbdyz.html&response_type=code&scope=snsapi_userinfo&state=STATE&connect_redirect=1#wechat_redirect\">请绑定加油站</a>");
        return res.toString();
    }

    @Override
    public String linkOpenidWithMobile(String openid, String mobile, String code) {
        String url = LINK_URL.replace("MOBILE", mobile).replace("CODE", code);
        url += openid;
        String res = restTemplate.getForObject(url, String.class);
        System.out.println(res + "\n");
        return JSON.toJSONString(res);
    }

    @Override
    public String fetchNickNameByOpenid(String openid) {
        long start = System.currentTimeMillis();
        String access_token = accessTokenService.getAccessToken();
        String url = USER_INFO_URL.replace("ACCESS_TOKEN", access_token).replace("OPENID", openid);
        String jsonStr = restTemplate.getForObject(url, String.class);
        String nickName = new String();
        try {
            JSONObject jsonObject = JSON.parseObject(jsonStr);
            nickName = jsonObject.getString("nickname");
        }catch (Exception e){
            e.printStackTrace();
        }
        long end = System.currentTimeMillis();
        System.out.println("[6]获取用户昵称用时: " + (end - start));
        return nickName;
    }
}
