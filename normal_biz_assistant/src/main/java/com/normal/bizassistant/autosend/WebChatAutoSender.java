package com.normal.bizassistant.autosend;

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
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */

@SpringBootApplication
public class WebChatAutoSender implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(WebChatAutoSender.class);

    @Autowired
    AutoSendManager autoSendManager;

    @Autowired
    Environment environment;

    private WindowsDriver driver;

    @Override
    public void run(String... args) throws Exception {
        initDriver();

        for (;;) {
            List<SendGood> goods = autoSendManager.querySendGoods();
            if (CollectionUtils.isEmpty(goods)) {
                logger.info("no goods anymore exit");
                break;
            }
            for (SendGood good : goods) {
                send(good);
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

                //send image
                groupEle.click();
                good.getImagePaths().forEach((image) -> groupEle.sendKeys(image));
                groupEle.sendKeys(Keys.ENTER);

                logger.info("good:{} had send", good);
            } catch (NoSuchElementException e) {
                logger.error("no such element by name:{}", group);
            }
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(WebChatAutoSender.class);
    }
}
