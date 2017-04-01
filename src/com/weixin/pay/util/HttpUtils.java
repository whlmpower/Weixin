package com.weixin.pay.util;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
/**
 * Created by luc on 16/8/6.
 */
public class HttpUtils {
    /**
     * 设置超时重连
     * @param url
     * @return
     */
    public static String doGet(String url) {
        String result = null;
        HttpClient client = null;
        long start = System.currentTimeMillis();
        try {
            client = new HttpClient();
            client.getHttpConnectionManager().getParams().setConnectionTimeout(300); //300
            client.getHttpConnectionManager().getParams().setSoTimeout(200); //200
            GetMethod method = new GetMethod(url);
            int statusCode = client.executeMethod(method);//状态，一般200为OK状态，其他情况会抛出如404,500,403等错误
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("远程访问失败。");
            }
            result = method.getResponseBodyAsString();
//            System.out.println("获取url数据时间: " + (System.currentTimeMillis() - start));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            client.getHttpConnectionManager().closeIdleConnections(1);
            System.out.println("获取数据超时,重连");
            return doGet(url);
        } finally {
            client.getHttpConnectionManager().closeIdleConnections(1);
        }
    }

    /**
     * 设置post超时2秒
     * 用于微信统一下单api
     * @param url
     * @param data
     * @return
     */
    public static String doPost(String url, String data){
        String result = null;
        HttpClient client = null;
        long start = System.currentTimeMillis();
        try {
            client = new HttpClient();//定义client对象
            client.getHttpConnectionManager().getParams().setConnectionTimeout(500);//设置连接超时时间为2秒（连接初始化时间）
            client.getHttpConnectionManager().getParams().setSoTimeout(500);
            PostMethod method = new PostMethod(url);
            client.getParams().setParameter(HttpMethodParams.HTTP_CONTENT_CHARSET, "utf-8");
            method.setRequestBody(data);
            int statusCode = client.executeMethod(method);
            if (statusCode != HttpStatus.SC_OK) {
                System.out.println("远程访问失败。");
            }
            result = method.getResponseBodyAsString();
//            System.out.println("获取url数据时间: " + (System.currentTimeMillis() - start));
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            client.getHttpConnectionManager().closeIdleConnections(1);
            System.out.println("获取数据超时,重连");
            return doPost(url, data);
        }finally {
            client.getHttpConnectionManager().closeIdleConnections(1);
        }

    }
}
