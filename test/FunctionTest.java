import com.spring.dao.AccessTokenDao;
import com.spring.dao.JsapiTicketDao;
import com.spring.service.AccessTokenService;
import com.spring.service.JsApiTicketService;
import com.weixin.pay.dao.TransationDao;
import com.weixin.pay.domain.generateOrder.OrderData;
import com.weixin.pay.service.*;
import com.weixin.util.WeixinUtil;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created by luc on 16/10/19.
 */
public class FunctionTest {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("/com/spring/config/bean.xml");
//        AdminService adminService = context.getBean(AdminService.class);
//        String result = adminService.queryTodayOrders("oNTV9wVvAizOxpXhl97jfHay7658", "1484035909");
//        adminService.linkOpenidWithMobile("oNTV9wVvAizOxpXhl97jfHay7658","13051877272","971");
//        System.out.println(result);
//        CacheService service = context.getBean(CacheService.class);
//        new Thread(() -> {
//            System.out.println(service.saveObject(1));
//            try {
//                Thread.sleep(500);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//            System.out.println(service.saveObject(1));
//        }).start();

//        AccessTokenDao accessTokenDao = context.getBean(AccessTokenDao.class);
//        accessTokenDao.insertAccessToken_u("bbb");
//        System.out.println(accessTokenDao.getaccess_token_u().getToken());
//        AccessTokenService accessTokenService = context.getBean(AccessTokenService.class);
//        System.out.println(accessTokenService.getAccessToken());

//        JsapiTicketDao jsapiTicketDao = context.getBean(JsapiTicketDao.class);
//        jsapiTicketDao.insertJsApiTicket_u("aaa");
//        JsApiTicketService jsApiTicketService = context.getBean(JsApiTicketService.class);
//        System.out.println(jsApiTicketService.getJsApiTicket());

//        WxPayService wxPayService = context.getBean(WxPayService.class);
//        System.out.println(wxPayService.fetchStationNameById("S01000022"));

    }
}
