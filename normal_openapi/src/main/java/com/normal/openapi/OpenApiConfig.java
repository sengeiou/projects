package com.normal.openapi;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author: fei.he
 */
@Configuration
@ComponentScan(basePackages = {"com.normal.openapi.impl"})
public class OpenApiConfig {

    @Autowired
    private Environment environment;

    @Bean
    public TaobaoClient taobaoClient() {
        return new DefaultTaobaoClient(environment.getProperty("autosend.taobao.serverurl"),
                environment.getProperty("autosend.taobao.appkey"),
                environment.getProperty("autosend.taobao.appsecret"));
    }
}
