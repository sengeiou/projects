package com.normal.bizassistant.autosend;

import com.normal.bizassistant.ConfigProperties;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */

@SpringBootApplication
public class WebChatAutoSender implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(WebChatAutoSender.class);


    @Autowired
    IOpenApiService openApiService;

    @Autowired
    Environment environment;

    private static WindowsDriver driver = null;


    static {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", ConfigProperties.getWebchatLocation());
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
            /*capabilities.setCapability("ms:waitForAppLaunch", "5");*/
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("e:{}", e);
        }
    }


    public static void main(String[] args) throws Exception {
        SpringApplication.run(WebChatAutoSender.class);
    }

    @Override
    public void run(String... args) throws Exception {
        Map<String, Object> params = new HashMap<>(1);

        for (int i = 1; ; i++) {
            params.put("pageNo", i);
            Iterator<SendGood> goodsIterator = openApiService.querySendGoods(params);
            if (goodsIterator == null) {
                logger.info("can not find goods info anymore, pageNo:{}", i);
                break;
            }
            for (; ; ) {

                if (!goodsIterator.hasNext()) {
                    break;
                }
                SendGood good = goodsIterator.next();
                send(good);
                Thread.sleep(Long.valueOf(environment.getProperty("autosend.interval.mills")));
            }
        }


    }

    private void send(SendGood good) {
        String groups = environment.getProperty("autosend.groups");
        for (String group : groups.split(",")) {
            try {
                WebElement groupEle = driver.findElementByName(group);
                //send text
                groupEle.click();
                groupEle.sendKeys(good.getText());
                groupEle.sendKeys(Keys.ENTER);

                //send images
                groupEle.click();

                byte[] imgsBytes = driver.pullFile(ConfigProperties.getGoodPicsPath());

                groupEle.sendKeys(Keys.ENTER);

            } catch (NoSuchElementException e) {
                logger.error("no such element by name:{}", group);
            }
        }

    }
}
