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

import java.net.URL;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */
public class WebChatAutoSender implements IAutoSender {
    private static WindowsDriver driver = null;
    public static final Logger logger = LoggerFactory.getLogger(WebChatAutoSender.class);

    static {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", ConfigProperties.getWebchatLocation());
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
//            capabilities.setCapability("ms:waitForAppLaunch", "5");
            driver = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            driver.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("e:{}", e);
        }
    }

    private final GoodProvider goodProvider;

    public WebChatAutoSender(GoodProvider goodProvider) {
        this.goodProvider = goodProvider;
    }

    static class SimpleGoodInfo {
        String text;
        byte[] imgs;
    }

    static class GoodProvider {

        public List<SimpleGoodInfo> provide() {
            return null;
        }
    }

    @Override
    public void send() {
        List<SimpleGoodInfo> goods = this.goodProvider.provide();
        if (CollectionUtils.isEmpty(goods)) {
            return;
        }

        List<String> groups = ConfigProperties.getGroups();
        for (SimpleGoodInfo good : goods) {
            for (String group : groups) {
                try {
                    WebElement groupEle = driver.findElementByName(group);
                    WebElement sendFileEle = driver.findElementByName("发送文件");

                    //send text
                    groupEle.click();
                    groupEle.sendKeys(good.text);
                    groupEle.sendKeys(Keys.ENTER);

                    //send images
                    sendFileEle.click();
                    byte[] imgsBytes = driver.pullFile(ConfigProperties.getGoodPicsPath());

                    groupEle.sendKeys(Keys.ENTER);
                } catch (NoSuchElementException e) {
                    logger.error("no such element by name:{}", group);
                }

            }
        }

    }

    public static void main(String[] args) throws Exception {
        WebChatAutoSender autoSender = new WebChatAutoSender(new GoodProvider());
        autoSender.send();
    }

}
