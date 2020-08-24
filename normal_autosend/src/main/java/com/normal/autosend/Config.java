package com.normal.autosend;

import com.normal.autosend.impl.BoundMaterialVoteSendGoodQueryStrategy;
import com.normal.autosend.impl.SendGoodQueryStrategy;
import com.normal.base.BaseConfig;
import com.normal.openapi.OpenApiConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.*;
import org.springframework.core.env.Environment;

import java.util.Arrays;
import java.util.List;

/**
 * @author fei.he
 */
@Configuration
@Import({BaseConfig.class, OpenApiConfig.class})
@ComponentScan(basePackages = {
        "com.normal.autosend.impl"
})
public class Config {

    @Autowired
    private Environment environment;

    @Bean
    public SendGoodQueryStrategy sendGoodQueryStrategy() {
        List<String> materials = Arrays.asList(environment.getProperty("autosend.taobao.materialids").split(","));
        return new BoundMaterialVoteSendGoodQueryStrategy(materials, "boundMaterialVote");
    }
}
