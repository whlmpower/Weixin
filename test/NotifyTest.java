import com.alibaba.fastjson.JSON;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.domain.generateOrder.OrderDataNotify;
import com.weixin.pay.util.CommonUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luc on 16/10/16.
 * 丢单 / 通知
 */
public class NotifyTest {
    private static final String WX_ORDER = "http://app1.u-coupon.cn:8000/weixin/wx_order.php?payid=PAYID&timestamp=TIMESTAMP&signiture=SIGN&&promotiondata=DATA";

    public static void main(String[] args) {

//        String data = "{\"openid\":\"oX0_Pwx8vnPffOKxs7WdVJBgj5Ws\",\"fee\":\"35\",\"amount\":\"10.00\",\"favorable\":\"9.65\",\"umoney_discount\":\"0.00\",\"coupon_discount\":\"9.65\",\"tel\":\"13051877272\",\"license\":\"京R14422\",\"station_id\":\"S0100002\",\"station_name\":\"北京测试加油站\",\"ticket\":\"0\",\"coupon_list\":[\"yh147382526113\",\"yh147382526114\",\"yh147382526116\",\"yh1470059866\"]}";
//        OrderData orderData = JSON.parseObject(data, OrderData.class);
//
//        StringBuffer buffer = new StringBuffer();
//        for(String s : orderData.getCoupon_list()){
//            buffer.append(s + ",");
//            System.out.println(s);
//        }
//        String list = StringUtils.substringBeforeLast(buffer.toString(), ",");

        String list = "yh1479563661";
        String out_trade_no = "2016101607160666897";

        String timestamp = CommonUtils.createTimeStampLast5();
        Map<String, String> params = new LinkedHashMap<>();
        params.put("payid", "2016101607473959577");
        params.put("timestamp", timestamp);
        String sign = CommonUtils.createUcouponSign(params);
        String url = WX_ORDER.replace("PAYID", "2016101607473959577").replace("TIMESTAMP", timestamp).replace("SIGN", sign).replace("DATA", list);
        System.out.println(url);

//        RestTemplate restTemplate = new RestTemplate();
//        String url = "http://wxoa.u-coupon.cn/Weixin/pay/notify.do";
//        String url = "http://localhost:8080/Weixin/pay/notify.do";
//        System.out.println(restTemplate.postForObject(url, JSON.toJSONString(orderDataNotify), String.class));
//        System.out.println(restTemplate.getForObject(url, String.class));
    }
}
