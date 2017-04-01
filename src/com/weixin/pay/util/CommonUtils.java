package com.weixin.pay.util;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by luc on 16/7/23.
 */
public class CommonUtils {

    private static final String KEY_CODE = "Up1@3$";

    public static String httpRequestByGet(String url) {
        String result = "null";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            return result;
        }
    }

    public static String doPostStr(String url, String outStr){
        DefaultHttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String result = null;
        try {
            httpPost.setEntity(new StringEntity(outStr, "UTF-8"));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            if(entity != null){
                result = EntityUtils.toString(entity, "UTF-8");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    public static String createUcouponSign(Map<String, String> map){
        String sign = null;
        StringBuffer buffer = new StringBuffer();
        for(Map.Entry<String, String> entry : map.entrySet()){
            String value = entry.getValue();
            buffer.append(value);
        }
        buffer.append(KEY_CODE);
        sign = MD5Util.MD5Encode(buffer.toString(), "utf-8").toUpperCase();
        return sign;
    }

    /**
     * 秒级时间戳后5位
     * @return
     */
    public static String createTimeStampLast5(){
        String time = "" + System.currentTimeMillis() / 1000;
        return time.substring(time.length() - 5, time.length());
    }

    public static void main(String[] args) {

        String timestamp = createTimeStampLast5();
        Map<String, String> map = new LinkedHashMap<>();
        map.put("payid", "2016080211063024964");
        map.put("timestamp", timestamp);
//        map.put("mobile","13051877272");
//        map.put("authcode","5724");
//        map.put("station_id", "S0100002");
//        map.put("timestamp", timestamp);
        String sign = createUcouponSign(map);
        System.out.println(timestamp);
        System.out.println(sign);

    }

}
