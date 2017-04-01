import com.weixin.pay.util.PayUtils;

import java.util.Map;
import java.util.TreeMap;

/**
 * Created by luc on 16/11/19.
 */
public class PayTest {

    static String QR_URL = "weixin://wxpay/bizpayurl?sign=SIGN&appid=APPID&mch_id=MID&product_id=PID&time_stamp=TS&nonce_str=NONCE";

    public static void main(String[] args) {
        /**
         * 生成扫码所需的参数
         */
        Map<Object, Object> params = PayUtils.createQRcode("S010001");
        System.out.println(params);
        QR_URL = QR_URL.replace("SIGN", String.valueOf(params.get("sign")))
                .replace("APPID", String.valueOf(params.get("appid")))
                .replace("MID", String.valueOf(params.get("mch_id")))
                .replace("PID", String.valueOf(params.get("product_id")))
                .replace("TS", String.valueOf(params.get("time_stamp")))
                .replace("NONCE", String.valueOf(params.get("nonce_str")));
        System.out.println(QR_URL);

    }
}
