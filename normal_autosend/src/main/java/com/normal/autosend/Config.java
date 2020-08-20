package com.normal.autosend;

import com.normal.base.BaseConfig;
import com.normal.openapi.OpenApiConfig;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

/**
 * @author fei.he
 */
@Configuration
@Import({BaseConfig.class, OpenApiConfig.class})
@ComponentScan(basePackages = {
        "com.normal.autosend.impl"
})
public class Config {


}
