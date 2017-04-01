package com.weixin.pay.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by luc on 16/11/19.
 */
@Controller
@RequestMapping("scan")
public class ScanPay {

    @RequestMapping("notify")
    String fetchScanResult(HttpServletRequest request) throws IOException {
        System.out.println("get req");
        InputStream inStream = request.getInputStream();
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
        while ((len = inStream.read(buffer)) != -1) {
            outSteam.write(buffer, 0, len);
        }
        outSteam.close();
        inStream.close();
        String result  = new String(outSteam.toByteArray(),"utf-8");
        System.out.println("callBack_xml>>>"+result);

        return "redirect:/wxpay/scan.html?id=s2001";
    }
}
