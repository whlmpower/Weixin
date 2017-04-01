package com.weixin.pay.util;

import com.weixin.util.DeveloperId;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.omg.CORBA.PUBLIC_MEMBER;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import java.io.*;
import java.net.ConnectException;
import java.net.URL;
import java.util.*;

/**
 * Created by luc on 16/7/19.
 */
public class PayUtils {

//    private static final String KEY = "44514808AaAa44514808AaAa44514800";

    /**
     * 当前毫秒时间戳
     * @return
     */
    public static long createTimeStamp(){
        return System.currentTimeMillis() + 43200000l + 3600000l;
    }

    /**
     * sign签名算法
     *
     * @param characterEncoding
     * @param parameters
     * @return
     */
    public static String createSign(String characterEncoding, SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            if (null != v && !"".equals(v)
                    && !"sign".equals(k) && !"key".equals(k)) {
                sb.append(k + "=" + v + "&");
            }
        }
        sb.append("key=" + DeveloperId.PAY_KEY);
        String sign = MD5Util.MD5Encode(sb.toString(), characterEncoding).toUpperCase();
        return sign;
    }

    /**
     * 生成统一订单的 请求的xml参数
     *
     * @param parameters
     * @return
     */
    public static String getRequestXml(SortedMap<Object, Object> parameters) {
        StringBuffer sb = new StringBuffer();
        sb.append("<xml>");
        Set es = parameters.entrySet();
        Iterator it = es.iterator();
        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            String v = (String) entry.getValue();
            sb.append("<" + k + ">" + v + "</" + k + ">");
        }
        sb.append("</xml>");
        return sb.toString();
    }

    public static String httpsRequest(String requestUrl, String requestMethod, String outputStr) {
        try {
            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
            TrustManager[] tm = {new MyX509TrustManager()};
            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
            sslContext.init(null, tm, new java.security.SecureRandom());
            // 从上述SSLContext对象中得到SSLSocketFactory对象
            SSLSocketFactory ssf = sslContext.getSocketFactory();
            URL url = new URL(requestUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(ssf);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            // 设置请求方式（GET/POST）
            conn.setRequestMethod(requestMethod);
            conn.setRequestProperty("content-type", "application/x-www-form-urlencoded");
            // 当outputStr不为null时向输出流写数据
            if (null != outputStr) {
                OutputStream outputStream = conn.getOutputStream();
                // 注意编码格式
                outputStream.write(outputStr.getBytes("UTF-8"));
                outputStream.close();
            }
            // 从输入流读取返回内容
            InputStream inputStream = conn.getInputStream();
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String str = null;
            StringBuffer buffer = new StringBuffer();
            while ((str = bufferedReader.readLine()) != null) {
                buffer.append(str);
            }
            // 释放资源
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
            inputStream = null;
            conn.disconnect();
            return buffer.toString();
        } catch (ConnectException ce) {
            System.out.println("连接超时：{}" + ce);
//            log.modelMsg("连接超时：{}", ce);
        } catch (Exception e) {
            System.out.println("https请求异常：{}" + e);
//            log.modelMsg("https请求异常：{}", e);
        }
        return null;
    }

    /**
     * 发送带xml参数的post请求
     *
     * @param url
     * @param outputStr
     * @return
     * @throws UnsupportedEncodingException
     */
    public static String httpRequestXml(String url, String outputStr) throws UnsupportedEncodingException {
        String result = null;
        HttpClient client = new DefaultHttpClient();
        HttpPost post = new HttpPost(url);
        StringEntity myEntity = new StringEntity(outputStr, "utf-8");
        post.addHeader("Content-Type", "text/xml");
        post.setEntity(myEntity);
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                result = EntityUtils.toString(entity, "utf-8");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;

    }

    public static String create_nonce_str() {
        UUID uuid = UUID.randomUUID();
        String nonce_str = uuid.toString().replaceAll("-", "");
        return nonce_str;
    }

    public static Map<Object, Object> createQRcode(String stationId){
        TreeMap<Object, Object> params = new TreeMap<>();
        params.put("appid", DeveloperId.APPID);
        params.put("mch_id", DeveloperId.MCH_ID);
        params.put("time_stamp", String.valueOf(createTimeStamp() / 1000));
        params.put("nonce_str", create_nonce_str());
        params.put("product_id", stationId);
        String sign = createSign("utf-8", params);
        params.put("sign", sign);
        return params;
    }
}
