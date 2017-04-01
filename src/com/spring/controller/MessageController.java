package com.spring.controller;

import com.material.MessageManage;
import com.weixin.pay.service.AdminService;
import com.weixin.pay.service.DateUtils;
import com.weixin.service.PayService;
import com.weixin.util.CheckUtil;
import com.weixin.util.MessageUtil;
import com.weixin.util.WeixinUtil;
import org.apache.commons.lang.StringUtils;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;

/**
 * Created by luc on 17/1/5.
 */
@Controller
public class MessageController {

    @Autowired
    private AdminService adminService;

    @RequestMapping(value = "ucoupon", method = RequestMethod.GET)
    public void messageGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String signature = req.getParameter("signature");
        String timestamp = req.getParameter("timestamp");
        String nonce = req.getParameter("nonce");
        String echostr = req.getParameter("echostr");

        PrintWriter out = resp.getWriter();
        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
            out.print(echostr);
        }
    }

    @RequestMapping(value = "ucoupon", method = RequestMethod.POST)
    public void messagePost(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        PayService payService = new PayService();
        PrintWriter out = resp.getWriter();
        MessageManage matrial = new MessageManage();

        try {
            Map<String, String> map = MessageUtil.xmlToMap(req);
            System.out.println();
            System.out.println("!!!!!!!!!!");
            System.out.print("收到的数据包:  " + map.toString() + " at ");
            DateUtils.printTimeStamp();
            String fromUserName = map.get("FromUserName");
            String toUserName = map.get("ToUserName");
            String createTime = map.get("CreateTime");
            String msgType = map.get("MsgType");
            String content = map.get("Content");
            String mediaId = map.get("MediaId");
            String location_X = map.get("Location_X");
            String location_Y = map.get("Location_Y");
            String scale = map.get("Scale");
            String label = map.get("Label");
            String url = map.get("Url");
            String event = map.get("Event");
            String eventKey = map.get("EventKey");
            String message = new String();
            if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
                if(StringUtils.equalsIgnoreCase(content, "CXJY") || StringUtils.equalsIgnoreCase(content, "1")){
                    String orderInfo = adminService.queryTodayOrders(fromUserName, createTime);
                    message = MessageUtil.initText(toUserName, fromUserName, orderInfo);
                }
                else if(StringUtils.equalsIgnoreCase(content, "BDYZ") || StringUtils.equalsIgnoreCase(content, "2")){
                    String bdInfo = adminService.queryBDStation(fromUserName);
                    message = MessageUtil.initText(toUserName, fromUserName, bdInfo);
                }
                else
                    message = MessageUtil.initText(toUserName, fromUserName, matrial.AutoMessage());
            }

            if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(event)) {
//                    eventKey = eventKey.substring(eventKey.indexOf('_') + 1);
                    message = MessageUtil.initText(toUserName, fromUserName, matrial.UcouponSub());
                    System.out.println(fromUserName + ": " + eventKey);
                    System.out.println("[scan]通知状态: " + WeixinUtil.sendQRResult2Ucoupon(fromUserName, eventKey, createTime));
                } else if (MessageUtil.EVENT_CLIK.equals(event)) {
                    if ("2".equals(eventKey)) {
                        message = MessageUtil.initText(toUserName, fromUserName, matrial.Menu2());
                    }
                    if ("3".equals(eventKey)) {
                        message = MessageUtil.initText(toUserName, fromUserName, matrial.Menu3());
                    }
                } else if (MessageUtil.EVENT_SCAN.equals(event)) {
                    /**
                     * 取消已关注的情况下,扫描场景二维码,发送消息
                     */
//                    message = MessageUtil.initText(toUserName, fromUserName, "欢迎回来 " + eventKey + " 号加油员为您服务");
                    System.out.println(fromUserName + ": " + eventKey);
                    System.out.println("[scan]通知状态: " + WeixinUtil.sendQRResult2Ucoupon(fromUserName, eventKey, createTime));
                }
            }

            System.out.println("########");
            System.out.println("发送的数据包如下 to: " + fromUserName);
            System.out.println(message);
            System.out.println("*****************");
            System.out.println();
            out.print(message);

        } catch (DocumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } finally {
            out.close();
        }
    }
}
