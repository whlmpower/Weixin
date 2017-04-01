package com.weixin.qr;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by luc on 16/12/16.
 */
@RestController
public class QRController {

    @Autowired
    private QRLinkService qrLinkService;

    private static String FETCH_QR = "https://mp.weixin.qq.com/cgi-bin/showqrcode?ticket=TICKET";

    @RequestMapping(value = "qr/{scene_id}")
    public Map<String, String> fetchQRQurl(@PathVariable(value = "scene_id") int scene_id){
        String url = FETCH_QR.replace("TICKET", qrLinkService.fetchQRTicket(scene_id));
        Map<String, String> map = new HashMap<>();
        map.put("url", url);
        return map;
    }

}
