package com.normal.bizassistant.autosend;

import com.normal.core.utils.ApplicationContextHolder;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;

/**
 * @author fei.he
 */
@Configuration
@MapperScan(basePackages = "com.normal.bizassistant.autosend")
public class AutoSendConfig {

    @Autowired
    private Environment environment;

    @Bean
    public TaobaoClient taobaoClient() {
        return new DefaultTaobaoClient(environment.getProperty("autosend.taobao.serverurl"),
                environment.getProperty("autosend.taobao.appkey"),
                environment.getProperty("autosend.taobao.appsecret"));
    }

    @Bean
    public ApplicationContextHolder holder() {
        return new ApplicationContextHolder();
    }


}
