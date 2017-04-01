package com.weixin.pay.controller;

import com.alibaba.fastjson.JSON;
import com.weixin.pay.domain.BdyzInfo;
import com.weixin.pay.domain.TimeStampObj;
import com.weixin.pay.domain.modelMsg.*;
import com.weixin.pay.service.AdminService;
import com.weixin.pay.service.ModelMsg;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by luc on 16/11/16.
 */
@Controller
//@Scope(value = "request")
public class BlockController {

    private TimeStampObj time = new TimeStampObj();

    private static ReentrantLock lock = new ReentrantLock();

    @Autowired
    private ModelMsg modelMsg;

    @Autowired
    private AdminService adminService;

    /**
     * 请求参数相同
     * controller会阻塞
     * 参数不同,相当于每个请求开启一个线程,不会造成阻塞
     * <p>
     * 参数相同,并不是spring阻塞,而是在chrome上测试,chrome针对同一url策略是排队请求
     *
     * @param status
     * @return
     */
    @RequestMapping(value = "block")
    @ResponseBody
    Map<String, String> getBlock(@RequestParam(value = "status") String status) {
        Map<String, String> map = new HashMap<>();
        map.put("status", "ok");
//        lock.lock();
        String threadName = Thread.currentThread().getName();
        System.out.println(threadName + " req get");
        if ("sleep".equals(status)) {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else if ("sleep2".equals(status)) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        System.out.println(threadName + " req finished");
        map.put("obj", time.toString());
//        lock.unlock();
        return map;
    }

    @RequestMapping(value = "util/bdyz", method = RequestMethod.POST)
    @ResponseBody
    String bdOpenidWithMobile(@RequestBody String json) throws UnsupportedEncodingException {
        json = URLDecoder.decode(json, "utf-8");
        int index = json.indexOf("=");
        if (index != -1)
            json = json.substring(0, json.indexOf("="));
        System.out.println(json);
        BdyzInfo data = JSON.parseObject(json, BdyzInfo.class);
        System.out.println("[从页面传来的绑定信息]: " + data);
        return JSON.toJSONString(adminService.linkOpenidWithMobile(data.getOpenid(), data.getMobile(), data.getAuth_code()));
    }

    @RequestMapping(value = "modelmsg/ticket_delay")
    @ResponseBody
    ModelMsgReqSuccess sendTicketDelay(@RequestParam(value = "openid", required = false) String openidStr,
                                       @RequestParam(value = "out_trade_no", required = false) String out_trade_no,
                                       @RequestParam(value = "station_name", required = false) String station_name,
                                       @RequestParam(value = "amount", required = false) String amount,
                                       @RequestParam(value = "moblie", required = false) String mobile,
                                       @RequestParam(value = "license", required = false) String license,
                                       @RequestParam(value = "time_end", required = false) String time_end) throws UnsupportedEncodingException {

        if (openidStr == null || out_trade_no == null || station_name == null || amount == null || mobile == null || license == null || time_end == null) {
            throw new ParamNullException();
        }
        if (openidStr.equals("") || out_trade_no.equals("") || station_name.equals("") || amount.equals("") || mobile.equals("") || license.equals("") || time_end.equals("")) {
            throw new ParamNullException();
        }

        /**
         * server已经将GET的编码方式配成 UTF-8 故不需要此转换
         *
         * BUT server这样设置会导致log乱码,故又修改回来
         */
        station_name = new String(station_name.getBytes("iso8859-1"), "UTF-8");
        license = new String(license.getBytes("iso8859-1"), "UTF-8");

        //转换金额格式
        amount = modelMsg.change2CNY(amount);

        //转换时间格式
        long timeStamp = Long.parseLong(time_end);
        time_end = modelMsg.changeDataFormat(timeStamp);

        ModelMsgReqSuccess respData = new ModelMsgReqSuccess();
        List<ModelMsgInfo> modelMsgInfos = new ArrayList<>();
        int msgSuccess = 0, msgFailure = 0;

        List<String> openidList = new ArrayList<>();
        for (String openid : openidStr.split(","))
            openidList.add(openid);
        respData.setMsg_num(openidList.size());
        /**
         * 给每个openid发送消息
         */
        for (String openid : openidList) {
            Map<String, String> map = new HashMap<>();
            map.put("first", "您好, " + time_end + " 有一笔交易,由于网络原因出票延迟\n");
            map.put("keyword1", out_trade_no);
            map.put("keyword2", station_name);
            map.put("keyword3", amount);
            map.put("keyword4", mobile);
            map.put("keyword5", license);
            map.put("remark", "\n网络恢复会补打票据,对于不便深表歉意");
            map.put("openid", openid);

            ModelMsgResp modelMsgResp = JSON.parseObject(modelMsg.sendTicketDelayMsg(map), ModelMsgResp.class);
            int code = modelMsgResp.getErrcode();
            if (code == 0)
                msgSuccess++;
            else
                msgFailure++;

            ModelMsgInfo modelMsgInfo = new ModelMsgInfo(modelMsgResp, openid);
            modelMsgInfos.add(modelMsgInfo);
        }

        respData.setCode(0);
        respData.setMsg("param is valid");
        respData.setMsg_success_num(msgSuccess);
        respData.setMsg_failure_num(msgFailure);
        respData.setMsg_detail(modelMsgInfos);

        return respData;
    }

    @ExceptionHandler(ParamNullException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ModelMsgReqFailure paramNullException() {
        ModelMsgReqFailure data = new ModelMsgReqFailure();
        data.setCode(1);
        data.setMsg("url param is invaild");
        return data;
    }

    @ExceptionHandler(NumberFormatException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public ModelMsgReqFailure amountInvaild() {
        ModelMsgReqFailure data = new ModelMsgReqFailure();
        data.setCode(2);
        data.setMsg("amount or time_end param is invaild");
        return data;
    }
}
