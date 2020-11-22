package com.normal.autosend.impl;

import com.normal.base.utils.Files;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.openapi.TbOpenApiQueryParam;
import com.normal.openapi.impl.OpenApiManager;
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

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
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

    private ScheduledExecutorService dailyExecutor = new ScheduledThreadPoolExecutor(
            1
    );

    @Autowired
    Environment environment;


    @Autowired
    OpenApiManager openApiManager;

    private WindowsDriver driver;


    List<DailyNoticeItem> dailyNoticeItems;

    int idx = 0;

    public void init() throws MalformedURLException {
        this.initDriver();
        this.initTask();
    }


    private void initTask() {
        //init data
        TbOpenApiQueryParam param = new TbOpenApiQueryParam().setTbMaterialId(Long.valueOf(BizDictEnums.OTHER_XPKSP.key()));
        dailyNoticeItems = openApiManager.tbQueryDailyNotices(param);
        //submit
        dailyExecutor.scheduleAtFixedRate(this::dailyTask, 0L, getPeriodSeconds(), TimeUnit.SECONDS);


    }

    private Long getPeriodSeconds() {
        return Long.valueOf(environment.getProperty("autosend.sendinterval.seconds"));
    }


    private synchronized void dailyTask() {
        try {
            if (idx == dailyNoticeItems.size() - 1) {
                idx = 0;
            }
            DailyNoticeItem item = this.dailyNoticeItems.get(idx);
            sendDailyNotices(item);
            idx++;
        } catch (Exception e) {
            logger.error("e: {}", e);
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


    public void sendDailyNotices(DailyNoticeItem item) {
        org.openqa.selenium.interactions.Actions actions = new Actions(driver);
        doSend(group -> {
            WebElement groupEle = driver.findElement(By.name(group));

            Iterator<String> iterator = Arrays.asList(item.getText().split("\n")).iterator();
            for (; iterator.hasNext(); ) {
                actions.sendKeys(Keys.SHIFT, Keys.ENTER)
                        .keyUp(Keys.SHIFT);
                String next = iterator.next();
                actions.sendKeys(next);
            }
            Files.ctrlC(new File(item.getImagePath()));
            actions.sendKeys(groupEle, Keys.CONTROL, "v")
                    .keyUp(Keys.CONTROL)
                    .sendKeys(Keys.ENTER)
                    .perform();

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
