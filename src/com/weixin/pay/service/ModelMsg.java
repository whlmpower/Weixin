package com.weixin.pay.service;

import com.alibaba.fastjson.JSON;
import com.spring.service.AccessTokenService;
import com.weixin.pay.domain.message.DetailMsg;
import com.weixin.pay.domain.message.paysuccess.TemplatePaySuccess;
import com.weixin.pay.domain.message.paysuccess.PaySuccess;
import com.weixin.pay.domain.message.ticketDelay.TemplateTicketDelay;
import com.weixin.pay.domain.message.ticketDelay.TicketDelay;
import com.weixin.pay.domain.message.ticketsuccess.TemplateTicketSuccess;
import com.weixin.pay.domain.message.ticketsuccess.TicketSuccess;
import com.weixin.pay.domain.message.ticketFailure.TemplateTicketFailure;
import com.weixin.pay.domain.message.ticketFailure.TicketFailure;
import com.weixin.pay.util.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.IllegalFormatConversionException;
import java.util.Locale;
import java.util.Map;

/**
 * Created by luc on 16/7/25.
 */
@Service
public class ModelMsg {

    private static final String MODEL_MSG = "https://api.weixin.qq.com/cgi-bin/message/template/send?access_token=ACCESS_TOKEN";

    @Autowired
    private AccessTokenService accessTokenService;

    //支付成功模板消息 定制方法
    public String sendPaySuccessMsg(Map<String, String> map) {

        DetailMsg first = new DetailMsg();
        first.setValue(map.get("first"));
        first.setColor("#828282");

        DetailMsg keyword1 = new DetailMsg();
        keyword1.setValue(map.get("keyword1"));

        DetailMsg keyword2 = new DetailMsg();
        keyword2.setValue(map.get("keyword2"));

        DetailMsg keyword3 = new DetailMsg();
        keyword3.setValue(map.get("keyword3"));

        DetailMsg keyword4 = new DetailMsg();
        keyword4.setValue(map.get("keyword4"));

        DetailMsg remark = new DetailMsg();
        remark.setValue(map.get("remark"));
        remark.setColor("#828282");

        PaySuccess paySuccess = new PaySuccess(first, keyword1, keyword2, keyword3, keyword4, remark);

        TemplatePaySuccess template = new TemplatePaySuccess(paySuccess);

        template.setTouser(map.get("openid"));

        String params = JSON.toJSONString(template);

        System.out.println("支付成功模板消息json: " + params);

        return doPostParams(params);

    }

    //出票成功模板消息定制方法
    public String sendTicketSuccessMsg(Map<String, String> map) {

        DetailMsg first = new DetailMsg();
        first.setValue(map.get("first"));
        first.setColor("#828282");

        DetailMsg keyword1 = new DetailMsg();
        keyword1.setValue(map.get("keyword1"));

        DetailMsg keyword2 = new DetailMsg();
        keyword2.setValue(map.get("keyword2"));

        DetailMsg keyword3 = new DetailMsg();
        keyword3.setValue(map.get("keyword3"));

        DetailMsg keyword4 = new DetailMsg();
        keyword4.setValue(map.get("keyword4"));

        DetailMsg remark = new DetailMsg();
        /**
         * remark设置成红色
         */
        remark.setColor("#FF3030");
        remark.setValue(map.get("remark"));

        TicketSuccess ticketSuccess = new TicketSuccess(first, keyword1, keyword2, keyword3, keyword4, remark);

        TemplateTicketSuccess templateTicket = new TemplateTicketSuccess(ticketSuccess);

        templateTicket.setTouser(map.get("openid"));

        String params = JSON.toJSONString(templateTicket);

        System.out.println("出票成功模板消息json: " + params);

        return doPostParams(params);

    }

    //出票失败模板消息组装
    public String sendTicketFailureMsg(Map<String, String> map) {

        DetailMsg first = new DetailMsg();
        first.setValue(map.get("first"));
        first.setColor("#828282");

        DetailMsg keyword1 = new DetailMsg();
        keyword1.setValue(map.get("keyword1"));

        DetailMsg keyword2 = new DetailMsg();
        keyword2.setValue(map.get("keyword2"));

        DetailMsg keyword3 = new DetailMsg();
        keyword3.setValue(map.get("keyword3"));

        DetailMsg remark = new DetailMsg();
        remark.setValue(map.get("remark"));
        remark.setColor("#828282");

        TicketFailure ticketFailure = new TicketFailure(first, keyword1, keyword2, keyword3, remark);

        TemplateTicketFailure template = new TemplateTicketFailure(ticketFailure);

        template.setTouser(map.get("openid"));

        String params = JSON.toJSONString(template);

        System.out.println("出票失败模板消息json: " + params);

        return doPostParams(params);
    }

    //出票延迟模板消息定制方法
    public String sendTicketDelayMsg(Map<String, String> map) {

        DetailMsg first = new DetailMsg();
        first.setValue(map.get("first"));
        first.setColor("#828282");

        DetailMsg keyword1 = new DetailMsg();
        keyword1.setValue(map.get("keyword1"));

        DetailMsg keyword2 = new DetailMsg();
        keyword2.setValue(map.get("keyword2"));

        DetailMsg keyword3 = new DetailMsg();
        keyword3.setValue(map.get("keyword3"));

        DetailMsg keyword4 = new DetailMsg();
        keyword4.setValue(map.get("keyword4"));

        DetailMsg keyword5 = new DetailMsg();
        keyword5.setValue(map.get("keyword5"));

        DetailMsg remark = new DetailMsg();
        /**
         * remark设置成灰色
         */
        remark.setColor("#828282");
        remark.setValue(map.get("remark"));

        TicketDelay ticketDelay = new TicketDelay(first, keyword1, keyword2, keyword3, keyword4, keyword5, remark);

        TemplateTicketDelay templateTicket = new TemplateTicketDelay(ticketDelay);

        templateTicket.setTouser(map.get("openid"));

        String params = JSON.toJSONString(templateTicket);

        System.out.println("出票延迟模板消息json: " + params);

        return doPostParams(params);

    }

    private String doPostParams(String params) {
//        String token = accessTokenCache.getAccessTokenByCache();
        String token = accessTokenService.getAccessToken();
        String url = MODEL_MSG.replace("ACCESS_TOKEN", token);
        String result = CommonUtils.doPostStr(url, params);
        return result;
    }

    /**
     * 将字符串的金额,转成 &yen; x.xx 格式
     * @param amout
     * @return
     */
    public String change2CNY(String amout){
        DecimalFormat df = new DecimalFormat("#.00");
        NumberFormat CHN = NumberFormat.getCurrencyInstance(Locale.CHINA);
        double amountDouble = 0;
        try {
            amountDouble = Double.parseDouble(amout);
            amountDouble = Double.parseDouble(df.format(amountDouble));
        }catch (NumberFormatException e){
            throw new NumberFormatException();
        }
        return CHN.format(amountDouble);
    }

    /**
     * 时间戳转成 2016-10-20 10:43:12
     * @return
     */
    public String changeDataFormat(long timeStamp){
        String time_end = String.valueOf(timeStamp);
        String reg = "(\\d{4})(\\d{2})(\\d{2})(\\d{2})(\\d{2})(\\d{2})";
        return time_end = time_end.replaceAll(reg, "$1-$2-$3 $4:$5:$6");
    }

}
