package com.normal.bizassistant.order;

import com.normal.bizassistant.ConfigProperties;
import com.normal.bizmodel.Order;
import com.normal.bizmodel.OrderStatus;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Response;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * @author: fei.he
 */
public class BizAssistant {
    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BizAssistant.class);

    LinkedBlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    ChromeDriver driver;

    /**
     * just for unit test
     */
    public BizAssistant() {
        initDriver();
    }

    public BizAssistant(Properties properties) {
        initDriver();
    }

    private void initDriver() {
        System.setProperty("webdriver.chrome.driver", ClassLoader.getSystemResource("chromedriver.exe").getFile());
        ChromeOptions options = new ChromeOptions();
        options.addArguments(new String[]{"--start-maximized"});
        options.setCapability("acceptSslCerts", true);
        options.setCapability("acceptInsecureCerts", true);
        this.driver = new ChromeDriver(options);
    }

    class Worker extends Thread {

        LinkedBlockingQueue<Order> queue;

        ChromeDriver driver;

        Properties properties;

        public Worker(int i, LinkedBlockingQueue<Order> queue, ChromeDriver driver, Properties properties) {
            this.setName("worker-thread" + i);
            this.queue = queue;
            this.driver = driver;
            this.properties = properties;
        }

        @Override
        public void run() {
            for (; ; ) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("worker thread had been interrupted return");
                    break;
                }
                try {
                    Order order = queue.take();
                    OrderStatus orderStatus = queryPaidUntilTimeout(order);
                    Map<String, String> rst = new HashMap<>(2);
                    rst.put("ordId", String.valueOf(order.getId()));
                    rst.put("orderStatus", orderStatus.toString());
                } catch (InterruptedException e) {
                    //clear interrupted status
                    Thread.interrupted();
                }
            }
        }

        private OrderStatus queryPaidUntilTimeout(Order order) throws InterruptedException {
            for (; ; ) {
                boolean expire = LocalDateTime.now().isAfter(order.getValidDateTime().plusSeconds(Long.valueOf(properties.getProperty("query.interval")) / 1000));
                if (expire) {
                    return OrderStatus.TIMEOUT;
                }
                Thread.sleep(Long.valueOf(properties.getProperty("query.interval")));
                driver.get(properties.getProperty("alipay.order.query.url"));
                Response response = (Response) driver.executeScript(properties.getProperty("js"));
                if (response == null) {
                    logger.error("js exe error, return rst is null");
                }
                List<Map<String, String>> aliOrder = convertToMap(response.getValue());
                if (aliOrder != null) {
                    boolean paid = aliOrder.stream()
                            .filter((item) -> String.valueOf(order.getPrice()).equals(item.get("totalPrice")) && item.get("payStatus").equals("成功"))
                            .count() > 0;
                    if (paid) {
                        return OrderStatus.PAIED;
                    }
                }
            }
        }

        private List<Map<String, String>> convertToMap(Object value) {
            logger.info("js exe rst data: {}", value);
            return null;
        }
    }

    public static void main(String[] args) throws Exception {
        //load config
         ConfigProperties.load();
        //init


    }



}
