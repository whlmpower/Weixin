//package com.weixin.servlet;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//import java.util.Date;
//import java.util.Map;
//import javax.servlet.ServletException;
//import javax.servlet.annotation.WebServlet;
//import javax.servlet.http.HttpServlet;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import com.weixin.pay.service.DateUtils;
//import com.weixin.qr.QRLinkService;
//import com.weixin.util.WeixinUtil;
//import org.dom4j.DocumentException;
//import com.material.MessageManage;
//import com.weixin.service.PayService;
//import com.weixin.util.CheckUtil;
//import com.weixin.util.MessageUtil;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.client.RestTemplate;
//
///**
// * Servlet implementation class Ucoupon
// */
//@WebServlet(urlPatterns = "/Ucoupon",
//        loadOnStartup = 1)
//public class Ucoupon extends HttpServlet {
//
//    private static final long serialVersionUID = 1L;
//
//    /**
//     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
//     */
//    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // TODO Auto-generated method stub
//        String signature = req.getParameter("signature");
//        String timestamp = req.getParameter("timestamp");
//        String nonce = req.getParameter("nonce");
//        String echostr = req.getParameter("echostr");
//
//        PrintWriter out = resp.getWriter();
//        if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
//            out.print(echostr);
//        }
//    }
//
//    /**
//     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
//     */
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        // TODO Auto-generated method stub
//        // TODO Auto-generated method stub
//        req.setCharacterEncoding("UTF-8");
//        resp.setCharacterEncoding("UTF-8");
//
//        PayService payService = new PayService();
//
//        PrintWriter out = resp.getWriter();
//        MessageManage matrial = new MessageManage();
//
//        try {
//            Map<String, String> map = MessageUtil.xmlToMap(req);
//            System.out.println();
//            System.out.println("!!!!!!!!!!");
//            System.out.print("收到的数据包:  " + map.toString() + " at ");
//            DateUtils.printTimeStamp();
//            String fromUserName = map.get("FromUserName");
//            String toUserName = map.get("ToUserName");
//            String createTime = map.get("CreateTime");
//            String msgType = map.get("MsgType");
//            String content = map.get("Content");
//            String mediaId = map.get("MediaId");
//            String location_X = map.get("Location_X");
//            String location_Y = map.get("Location_Y");
//            String scale = map.get("Scale");
//            String label = map.get("Label");
//            String url = map.get("Url");
//            String event = map.get("Event");
//            String eventKey = map.get("EventKey");
//            String message = null;
//            if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
//                message = MessageUtil.initText(toUserName, fromUserName, matrial.AutoMessage());
//            }
//
//            if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
//                if (MessageUtil.MESSAGE_SUBSCRIBE.equals(event)) {
////                    eventKey = eventKey.substring(eventKey.indexOf('_') + 1);
//                    message = MessageUtil.initText(toUserName, fromUserName, matrial.UcouponSub());
//                    System.out.println(fromUserName + ": " + eventKey);
//                    System.out.println("[scan]通知状态: " + WeixinUtil.sendQRResult2Ucoupon(fromUserName, eventKey, createTime));
//                } else if (MessageUtil.EVENT_CLIK.equals(event)) {
//                    if ("2".equals(eventKey)) {
//                        message = MessageUtil.initText(toUserName, fromUserName, matrial.Menu2());
//                    }
//                    if ("3".equals(eventKey)) {
//                        message = MessageUtil.initText(toUserName, fromUserName, matrial.Menu3());
//                    }
//                } else if (MessageUtil.EVENT_SCAN.equals(event)) {
//                    message = MessageUtil.initText(toUserName, fromUserName, "欢迎回来 " + eventKey + " 号加油员为您服务");
//                    System.out.println(fromUserName + ": " + eventKey);
//                    System.out.println("[scan]通知状态: " + WeixinUtil.sendQRResult2Ucoupon(fromUserName, eventKey, createTime));
//                }
//            }
//
//            System.out.println("########");
//            System.out.println("发送的数据包如下 to: " + fromUserName);
//            System.out.println(message);
//            System.out.println("*****************");
//            System.out.println();
//            out.print(message);
//
//        } catch (DocumentException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } finally {
//            out.close();
//        }
//    }
//}
