package com.weixin.qr;

import com.alibaba.fastjson.JSON;
import com.spring.service.AccessTokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * Created by luc on 16/12/16.
 */
@Service
public class QRLinkService {

    private static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/qrcode/create?access_token=TOKEN";

    private static String UCOUPON_SCENE_URL = "http://app1.u-coupon.cn:8000/weixin/label_code.php?openid=OPENID&scene_id=SCENE&addtime=CREATETIME";

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private AccessTokenService accessTokenService;
    /**
     * 获取永久二维码,ticket
     *
     * @return
     */
    public String fetchQRTicket(int scene_id) {
        String access_token = accessTokenService.getAccessToken();
        String url = TICKET_URL.replace("TOKEN", access_token);
        String qrdata = JSON.toJSONString(new QRData(scene_id));
        System.out.println(qrdata);
        String result = restTemplate.postForObject(url, qrdata, String.class);
        System.out.println("ticket: " + result);
        String ticket = JSON.parseObject(result).getString("ticket");
        return ticket;
    }

    /**
     * 将扫码结果传递给后台
     *
     * @return
     */
    public String sendQRResult2Ucoupon(String openid, String eventKey, String createTime) {
        String url = UCOUPON_SCENE_URL.replace("OPENID", openid)
                .replace("SCENE", eventKey)
                .replace("CREATETIME", createTime);
        String result = new String();
        try {
            result = restTemplate.getForObject(url, String.class);
        }catch (Exception e){
            e.printStackTrace();
        }
        return result;
    }


    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("com/spring/config/bean.xml");
        QRLinkService service = context.getBean(QRLinkService.class);
        System.out.println(service.fetchQRTicket(11));
    }
}
