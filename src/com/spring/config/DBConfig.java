package com.spring.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.weixin.util.DeveloperId;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * Created by luc on 16/11/21.
 */
@Configuration
public class DBConfig {

    @Bean
    @Qualifier("my")
    public DruidDataSource druid(){
        DruidDataSource ds = null;
        Properties props = new Properties();
        String path = "myDB.properties";
        try {
            props.load(DBConfig.class.getClassLoader().getResourceAsStream(path));
            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    @Bean
    @Qualifier("ucoupon")
    public DruidDataSource ucoupon(){
        DruidDataSource ds = null;
        Properties props = new Properties();
        String path = "ucouponDB.properties";
        try {
            props.load(DBConfig.class.getClassLoader().getResourceAsStream(path));
            ds = (DruidDataSource) DruidDataSourceFactory.createDataSource(props);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ds;
    }

    @Bean
    @Primary
    @Qualifier("myjdbc")
    public JdbcTemplate jdbcTemplate(@Qualifier("my")DataSource ds){
        return new JdbcTemplate(ds);
    }

    @Bean
    @Primary
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate(@Qualifier("my")DataSource ds){
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean
    @Qualifier("ucouponnamedjdbc")
    public NamedParameterJdbcTemplate namedParameterJdbcTemplateUcoupon(@Qualifier("ucoupon")DataSource ds){
        return new NamedParameterJdbcTemplate(ds);
    }

    @Bean
    @Qualifier("ucouponjdbc")
    public JdbcTemplate jdbcTemplateUcoupon(@Qualifier("ucoupon")DataSource ds){
        return new JdbcTemplate(ds);
    }

}
