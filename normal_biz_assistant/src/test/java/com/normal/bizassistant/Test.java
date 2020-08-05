package com.normal.bizassistant;

import org.junit.Before;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author: fei.he
 */
public class Test {
    ChromeDriver driver;

    @Before
    public void before() {
        System.setProperty("webdriver.chrome.driver", "D:\\doc\\项目点\\东莞\\3.本币新平台交易接口验收辅助工具\\xQuant-Auto-Upload-V1.0.3\\driver\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments(new String[]{"--start-maximized"});
        options.setCapability("acceptSslCerts", true);
        options.setCapability("acceptInsecureCerts", true);
        driver = new ChromeDriver();
    }

    @org.junit.Test
    public void test() {
        driver.get("https://www.baidu.com/");
        driver.executeScript("alert('dd')");
    }
}
