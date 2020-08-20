package com.normal.autosend;

import com.normal.autosend.impl.AutoSendManager;
import com.normal.base.utils.Files;
import com.normal.model.autosend.SendGood;
import io.appium.java_client.windows.WindowsDriver;
import org.apache.commons.collections.CollectionUtils;
import org.openqa.selenium.By;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author: fei.he
 */
@SpringBootApplication
public class App implements CommandLineRunner {

    public static final Logger logger = LoggerFactory.getLogger(App.class);

    @Autowired
    AutoSendManager autoSendManager;

    @Autowired
    Environment environment;

    private WindowsDriver driver;

    @Override
    public void run(String... args) throws Exception {
        initDriver();
        for (; ; ) {
            List<SendGood> goods = autoSendManager.querySendGoods();
            if (CollectionUtils.isEmpty(goods)) {
                logger.info("no goods anymore exit");
                break;
            }
            for (SendGood good : goods) {
                send(good);
                TimeUnit.SECONDS.sleep(Long.valueOf(environment.getProperty("autosend.sendinterval.seconds")));
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
        Actions actions = new Actions(driver);
        for (String group : groups.split(",")) {
            try {

                WebElement groupEle = driver.findElement(By.name(group));
                //send text
                Iterator<String> iterator = Arrays.asList(good.getText().split("\n"))
                        .iterator();
                String textItem = iterator.next();
                actions.sendKeys(groupEle, textItem);

                for (; iterator.hasNext(); ) {
                    actions.sendKeys(Keys.SHIFT, Keys.ENTER)
                            .keyUp(Keys.SHIFT);
                    actions.sendKeys(iterator.next());
                }
                actions.sendKeys(Keys.ENTER).perform();

                //send images
                for (String imagePath : good.getImagePaths()) {
                    Files.ctrlC(new File(imagePath));

                    actions.sendKeys(groupEle, Keys.CONTROL, "v")
                            .keyUp(Keys.CONTROL)
                            .sendKeys(Keys.ENTER)
                            .perform();
                }
                autoSendManager.updateSendGoodsStatus(good.getId());
            } catch (NoSuchElementException e) {
                logger.error("no such element found:{}", e);
            }
        }

    }

    public static void main(String[] args) {
        SpringApplication.run(App.class);
    }
}
