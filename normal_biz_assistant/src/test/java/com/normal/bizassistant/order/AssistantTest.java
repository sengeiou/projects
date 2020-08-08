package com.normal.bizassistant.order;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.util.Properties;

/**
 * @author: fei.he
 */
public class AssistantTest {
    public static final Logger logger = LoggerFactory.getLogger(AssistantTest.class);

    private BizAssistant bizAssistant;


    @Test
    public void test() throws Exception {
        Properties properties = BizAssistant.loadProperties();
        this.bizAssistant = new BizAssistant();
        String js = properties.getProperty("js");
        logger.info("js: {}", js);
        Object response = bizAssistant.driver.executeScript(js);
        logger.info(response.toString());
    }

    public void after() {
        bizAssistant.driver.quit();
    }
}
