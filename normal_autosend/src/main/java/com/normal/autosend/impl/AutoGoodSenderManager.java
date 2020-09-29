package com.normal.autosend.impl;

import com.normal.base.utils.Files;
import com.normal.model.autosend.SendGood;
import io.appium.java_client.windows.WindowsDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author: fei.he
 */
@Component
public class AutoGoodSenderManager {
    public static final Logger logger = LoggerFactory.getLogger(AutoGoodSenderManager.class);

    private ScheduledExecutorService executorService = new ScheduledThreadPoolExecutor(
            3
    );

    @Autowired
    Environment environment;

    @Autowired
    FacadeAutoSendServiceWrapper facadeAutoSendServiceWrapper;

    private WindowsDriver driver;

    private LinkedList<SendGood> dailyGoodList = new LinkedList<>();

    @PostConstruct
    public void init() throws MalformedURLException {
        this.initDriver();
        this.initTask();
    }

    private void initTask() {
        Long periodSecond = Long.valueOf(environment.getProperty("autosend.sendinterval.seconds"));
        // 每日任务
        executorService.scheduleAtFixedRate(() -> {
            String notices = facadeAutoSendServiceWrapper.queryNotices();
            sendDailyNotices(notices);
        }, getInitalDelaySecond(), TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

        //固定任务
        executorService.schedule(() -> {
            if (dailyGoodList.poll() == null) {
                List<SendGood> goods = facadeAutoSendServiceWrapper.querySendGoods();
                for (SendGood good : goods) {
                    dailyGoodList.offer(good);
                }
            }
            sendGood(dailyGoodList.poll());
        }, periodSecond, TimeUnit.SECONDS);
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

    private long getInitalDelaySecond() {
        LocalDate now = LocalDate.now();
        now.plusDays(1);
        LocalDateTime towMorning = LocalDateTime.of(now, LocalTime.of(8, 30));
        Duration duration = Duration.between(LocalDateTime.now(), towMorning);
        return duration.getSeconds();
    }

    public synchronized void sendDailyNotices(String notices) {
         doSend(group -> {
             WebElement groupEle = driver.findElement(By.name(group));
             groupEle.sendKeys(notices);
             groupEle.sendKeys(Keys.ENTER);
             return null;
         });
    }

    public synchronized void sendGood(SendGood good) {
        org.openqa.selenium.interactions.Actions actions = new Actions(driver);
        doSend(group -> {
            WebElement groupEle = driver.findElement(By.name(group));
            //sendGood text
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
            logger.info("sendGood text for good id :{}", good.getCategoryId());
            //sendGood images
            for (String imagePath : good.getImagePaths()) {
                Files.ctrlC(new File(imagePath));
                logger.info("copy image from path: {}", imagePath);
                actions.sendKeys(groupEle, Keys.CONTROL, "v")
                        .keyUp(Keys.CONTROL)
                        .sendKeys(Keys.ENTER)
                        .perform();
            }
            return null;
        });
    }

    private synchronized void doSend(Function<String, Void> callback) {
        String groups = environment.getProperty("autosend.groups");
        for (String group : groups.split(",")) {
            try {
                callback.apply(group);
            } catch (NoSuchElementException e) {
                logger.error("no such element found:{}", e);
            }
        }
    }

}
