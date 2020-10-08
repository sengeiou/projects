package com.normal.autosend.impl;

import com.normal.base.utils.Files;
import com.normal.dao.context.BizContextMapper;
import com.normal.model.BizCodes;
import com.normal.model.BizDictEnums;
import com.normal.model.autosend.DailyNoticeItem;
import com.normal.model.autosend.SendGood;
import com.normal.model.context.BizContextTypes;
import com.normal.model.openapi.DefaultPageOpenApiQueryParam;
import com.normal.model.openapi.OpenApiEvent;
import com.normal.openapi.IOpenApiService;
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
import org.springframework.context.ApplicationListener;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

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
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

/**
 * @author: fei.he
 */
@Component
public class AutoGoodSenderManager implements ApplicationListener<OpenApiEvent> {
    public static final Logger logger = LoggerFactory.getLogger(AutoGoodSenderManager.class);

    private ScheduledExecutorService dailyExecutor = new ScheduledThreadPoolExecutor(
            1
    );

    private ScheduledExecutorService periodExecutor = new ScheduledThreadPoolExecutor(
            1
    );

    @Autowired
    Environment environment;

    @Autowired
    FacadeAutoSendServiceWrapper facadeAutoSendServiceWrapper;

    @Autowired
    BizContextMapper bizContextMapper;

    @Autowired
    IOpenApiService openApiService;

    private WindowsDriver driver;

    private LinkedList<SendGood> dailyGoodList = new LinkedList<>();

    ScheduledFuture currPeriodFuture;

    ScheduledFuture currDailyFuture;

    public void init() throws MalformedURLException {
        this.initDriver();
        this.initTask();
    }


    private void initTask() {
        Long periodSecond = getPeriodSeconds();
        // 每日任务
        long initialDelaySecond = getInitialDelaySecond();

        currDailyFuture = dailyExecutor.scheduleAtFixedRate(() -> {
            dailyTask();
        }, initialDelaySecond, TimeUnit.DAYS.toSeconds(1), TimeUnit.SECONDS);

        //固定任务
        currPeriodFuture = periodExecutor.scheduleAtFixedRate(() -> {
            periodTask();
    }, 0L, periodSecond, TimeUnit.SECONDS);
}

    private Long getPeriodSeconds() {
        return Long.valueOf(environment.getProperty("autosend.sendinterval.seconds"));
    }

    private synchronized void periodTask() {
        SendGood item = dailyGoodList.poll();
        logger.info("----peroid task --- good: {}");

        if (item == null) {
            List<SendGood> goods = facadeAutoSendServiceWrapper.querySendGoods();
            for (SendGood good : goods) {

                dailyGoodList.offer(good);
            }
        }
        sendGood(item == null ? dailyGoodList.poll() : item);
    }

    private synchronized void dailyTask() {
        DefaultPageOpenApiQueryParam param = DefaultPageOpenApiQueryParam.newInstance().withQueryType(BizDictEnums.OTHER_XPKSP);
        List<DailyNoticeItem> rsts = openApiService.queryDailyGoods(param);
        sendDailyNotices(rsts);
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

    private static long getInitialDelaySecond() {
        LocalDate now = LocalDate.now();
        LocalDate tow = now.plusDays(1);
        LocalDateTime towMorning = LocalDateTime.of(tow, LocalTime.of(8, 30));
        Duration duration = Duration.between(LocalDateTime.now(), towMorning);
        return duration.getSeconds();
    }


    private void sendText(Actions actions, String textWithEnter, WebElement groupEle) {
        Iterator<String> iterator = Arrays.asList(textWithEnter.split("\n"))
                .iterator();
        String textItem = iterator.next();
        actions.sendKeys(groupEle, textItem);

        for (; iterator.hasNext(); ) {
            actions.sendKeys(Keys.SHIFT, Keys.ENTER)
                    .keyUp(Keys.SHIFT);
            actions.sendKeys(iterator.next());
        }
        actions.sendKeys(Keys.ENTER).perform();
    }

    /**
     * 发送每日消息
     *
     * @param notices
     */
    public void sendDailyNotices(List<DailyNoticeItem> notices) {
        org.openqa.selenium.interactions.Actions actions = new Actions(driver);
        doSend(group -> {
            WebElement groupEle = driver.findElement(By.name(group));
            actions.sendKeys("♡♡♡今日优惠推荐♡♡♡").sendKeys(Keys.ENTER).perform();

            for (DailyNoticeItem notice : notices) {
                Iterator<String> iterator = Arrays.asList(notice.getText().split("\n")).iterator();
                for (; iterator.hasNext(); ) {
                    actions.sendKeys(Keys.SHIFT, Keys.ENTER)
                            .keyUp(Keys.SHIFT);
                    String next = iterator.next();
                    actions.sendKeys(next);
                }
                Files.ctrlC(new File(notice.getImagePath()));
                actions.sendKeys(groupEle, Keys.CONTROL, "v")
                        .keyUp(Keys.CONTROL)
                        .sendKeys(Keys.ENTER)
                        .perform();

            }
            return null;
        });
    }

    /**
     * 间隔发送商品信息
     *
     * @param good
     */
    public void sendGood(SendGood good) {
        if (good == null) {
            logger.error(" good 为空");
            return;
        }
        org.openqa.selenium.interactions.Actions actions = new Actions(driver);
        doSend(group -> {
            WebElement groupEle = driver.findElement(By.name(group));
            sendText(actions, good.getText(), groupEle);
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

    @Override
    public void onApplicationEvent(OpenApiEvent openApiEvent) {
        //查询出错, 删除查询上下文, 从头开始查
        if (BizCodes.OPEN_API_ERRORCODE_50001.equals(openApiEvent.getSource())) {
            bizContextMapper.deleteByType(BizContextTypes.querySendGood);
            logger.info("查询出错, 删除查询上下文, 从头开始查");
            if (currPeriodFuture != null) {
                currPeriodFuture.cancel(false);
                periodExecutor.scheduleAtFixedRate(this::periodTask, 0L, getPeriodSeconds(), TimeUnit.SECONDS);
            }
        }
    }
}
