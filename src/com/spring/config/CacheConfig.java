package com.spring.config;

import com.weixin.pay.service.CacheService;
import net.sf.ehcache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.cache.ehcache.EhCacheCacheManager;
import org.springframework.cache.ehcache.EhCacheManagerFactoryBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.ClassPathResource;

/**
 * Created by luc on 16/10/16.
 */
@Configuration
@EnableCaching
@ComponentScan(basePackageClasses = CacheService.class)
public class CacheConfig {

//    @Bean
//    public org.springframework.cache.CacheManager cacheManager(){
//        return new ConcurrentMapCacheManager();
//    }

    @Bean
    public EhCacheCacheManager cacheManager(CacheManager cm){
        return new EhCacheCacheManager(cm);
    }

    @Bean
    public EhCacheManagerFactoryBean ehcache(){
        EhCacheManagerFactoryBean ehCacheManagerFactoryBean = new EhCacheManagerFactoryBean();
        ehCacheManagerFactoryBean.setConfigLocation(new ClassPathResource("/com/spring/cache/ehcache.xml"));
        return ehCacheManagerFactoryBean;
    }
}
