package com.normal.bizassistant.marketing;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */
public class AutoSender {
    private static WindowsDriver session = null;
    public static final Logger logger = LoggerFactory.getLogger(AutoSender.class);

    static {
        try {
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability("app", "E:\\soft-install-here\\WeChat\\WeChat.exe");
            capabilities.setCapability("platformName", "Windows");
            capabilities.setCapability("deviceName", "WindowsPC");
//            capabilities.setCapability("ms:waitForAppLaunch", "5");
            session = new WindowsDriver(new URL("http://127.0.0.1:4723"), capabilities);
            session.manage().timeouts().implicitlyWait(2, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("e:{}", e);
        }
    }

    public void send() {
        WebElement element = session.findElementByName("文件传输助手");
        element.click();
        element.sendKeys("这里是测试内容");

        element.sendKeys(Keys.ENTER);
    }

    public static void main(String[] args) throws Exception {
        AutoSender autoSender = new AutoSender();
        autoSender.send();
    }

}
