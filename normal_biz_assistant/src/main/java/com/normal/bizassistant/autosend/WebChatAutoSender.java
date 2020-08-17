package com.normal.bizassistant.autosend;

import io.appium.java_client.windows.WindowsDriver;
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
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */

@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class WebChatAutoSender implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(WebChatAutoSender.class);


    @Autowired
    IOpenApiService openApiService;

    @Autowired
    Environment environment;

    private WindowsDriver driver;


    @Override
    public void run(String... args) throws Exception {
        initDriver();
        Map<String, Object> params = new HashMap<>(1);

        for (int i = 1; ; i++) {
            params.put("pageNo", i);
            params.put("materialId", environment.getProperty("autosend.materialid")); //数码家电
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
                logger.info("send good: {}", good);
                send(good);
                Thread.sleep(Long.valueOf(environment.getProperty("autosend.interval.mills")));
            }
        }


    }

    private void initDriver() throws MalformedURLException {
        DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setCapability("app", environment.getProperty("autosend.webchat.location"));
        capabilities.setCapability("platformName", "Windows");
        capabilities.setCapability("deviceName", "WindowsPC");
        /*capabilities.setCapability("ms:waitForAppLaunch", "5");*/
        driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
        driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);

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

                //send imagePaths
                groupEle.click();
                good.getImagePaths().forEach((image) -> groupEle.sendKeys(image));
                groupEle.sendKeys(Keys.ENTER);

            } catch (NoSuchElementException e) {
                logger.error("no such element by name:{}", group);
            }
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(WebChatAutoSender.class);
    }
}
