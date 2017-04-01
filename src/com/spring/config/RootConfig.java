package com.spring.config;

import com.weixin.util.DeveloperId;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Properties;

/**
 * Created by luc on 16/9/25.
 */
@Configuration
@EnableAsync
@Import({CacheConfig.class, DBConfig.class})
public class RootConfig {

    static {
        Properties properties = new Properties();
        String path = "wechat.properties";
        try {
            properties.load(DBConfig.class.getClassLoader().getResourceAsStream(path));
            DeveloperId.APPID = properties.getProperty("APPID");
            DeveloperId.APPSECRET = properties.getProperty("APPSECRET");
            DeveloperId.MCH_ID = properties.getProperty("MCH_ID");
            DeveloperId.PAY_KEY = properties.getProperty("PAY_KEY");
            DeveloperId.GMAP_KEY = properties.getProperty("GMAP_KEY");
            DeveloperId.GMAP_KEY_MY = properties.getProperty("GMAP_KEY_MY");
            System.out.println("wechat config load successful");
        } catch (IOException e) {
            System.out.println("wechat config load failure");
            e.printStackTrace();
        }
    }

    @Bean
    public RestTemplate restTemplate(){
        System.setProperty("java.net.preferIPv4Stack" , "true");
        System.out.println("!!!!rest!!!!");
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
        return restTemplate;
    }

}
