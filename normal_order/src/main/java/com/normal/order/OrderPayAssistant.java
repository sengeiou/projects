package com.normal.order;

import com.normal.communicate.client.BizClient;
import com.normal.communicate.client.BizClientHandler;
import com.normal.communicate.client.ClientRecvListener;
import com.normal.communicate.client.ConfigProperties;
import com.normal.base.utils.Jsons;
import com.normal.model.*;
import com.normal.model.communicate.DuplexMsg;
import com.normal.model.order.Order;
import com.normal.model.order.OrderStatus;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Response;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * @author: fei.he
 */
public class OrderPayAssistant {
    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(OrderPayAssistant.class);

    ChromeDriver driver;


    /**
     * just for unit test
     */
    public OrderPayAssistant() {
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


    public OrderStatus queryPaidUntilTimeout(Order order) {
        for (; ; ) {
            boolean expire = LocalDateTime.now().isAfter(order.getValidDateTime().plusSeconds(Long.valueOf(ConfigProperties.getProperty("biz.client.query.interval")) / 1000));
            if (expire) {
                return OrderStatus.TIMEOUT;
            }
            try {
                Thread.sleep(Long.valueOf(ConfigProperties.getProperty("biz.client.query.interval")));
            } catch (InterruptedException e) {
                Thread.interrupted();
            }
            driver.get(ConfigProperties.getProperty("biz.client.alipay.order.query.url"));
            Response response = (Response) driver.executeScript(ConfigProperties.getProperty("biz.client.order.js"));
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

    public static void main(String[] args) throws Exception {
        ConfigProperties.load();
        BizClient client = new BizClient();
        Runtime.getRuntime().addShutdownHook(new Thread(() -> client.close()));

        BizClientHandler bizClientHandler = client.getBizClientHandler();
        bizClientHandler.addListener(new ClientRecvListener() {
            @Override
            public String[] codes() {
                return new String[]{
                        BizCodes.ORDER_QUERY_ALIPAY_ORDERS
                };
            }

            @Override
            public void recv(DuplexMsg rst) {
                OrderPayAssistant assistant = new OrderPayAssistant();
                if (BizCodes.ORDER_QUERY_ALIPAY_ORDERS.equals(rst.getCode())) {

                    Order order = Jsons.toObj(rst.getData(), Order.class);
//                    OrderStatus orderStatus = assistant.queryPaidUntilTimeout(order);
                    order.setOrderStatus(OrderStatus.PAIED);
                    logger.info("recv server msg:{}", rst);
                    bizClientHandler.send(new DuplexMsg(BizCodes.ORDER_STATUS_RECV, Jsons.toJson(order)));
                }

            }
        });


    }


}
