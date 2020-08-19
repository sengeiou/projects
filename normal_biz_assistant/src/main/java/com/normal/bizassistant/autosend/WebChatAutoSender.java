package com.normal.bizassistant.autosend;

import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */

@SpringBootApplication(scanBasePackages = {"com.normal.bizassistant"})
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
        send(null);
        /*for (; ; ) {
            List<SendGood> goods = autoSendManager.querySendGoods();
            if (CollectionUtils.isEmpty(goods)) {
                logger.info("no goods anymore exit");
                break;
            }
            for (SendGood good : goods) {
                send(good);
                TimeUnit.SECONDS.sleep(Long.valueOf(environment.getProperty("autosend.sendinterval.seconds")));
            }
        }*/
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
        Actions actions = new Actions(driver);
        for (String group : groups.split(",")) {
            try {
                WebElement groupEle = driver.findElementByName(group);
                actions.sendKeys(groupEle, "123")
                        .sendKeys(Keys.SHIFT, Keys.ENTER)
                        .sendKeys("456")
                        .sendKeys(Keys.ENTER)
                        .perform();



                //send text
                /*groupEle.click();
                actions.
                groupEle.sendKeys(Keys.ENTER);

                //send image
                groupEle.click();
                good.getImagePaths().forEach((image) -> groupEle.sendKeys(environment.getProperty("autosend.images.path") + image));

                groupEle.sendKeys(Keys.ENTER);
                logger.info("good:{} had send", good);
                autoSendManager.updateSendGoodsStatus(good.getId());*/

            } catch (NoSuchElementException e) {
                logger.error("no such element by name:{}", group);
            }
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(WebChatAutoSender.class);
    }
}
