package com.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by luc on 17/3/4.
 */
@RestController
@RequestMapping(value = "ofoutil")
public class OfoController {

    @RequestMapping(value = "fetch_code")
    public Map<String, String> getUserOpenid(@RequestParam(value = "openid",defaultValue = "error") String openid) throws InterruptedException {
        System.out.println("get user [ " + openid + " ] req");
        Map<String, String> res = new HashMap<>();
        res.put("status", "0");
        res.put("code", "1000");
        res.put("openid", openid);
        Thread.sleep(2000);
        return res;
    }
}
