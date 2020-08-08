package com.normal.bizassistant.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizassistant.ConfigProperties;
import com.normal.bizmodel.Order;
import com.normal.bizmodel.OrderStatus;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.Response;

import java.io.*;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class BizAssistant {
    public static final org.slf4j.Logger logger = org.slf4j.LoggerFactory.getLogger(BizAssistant.class);


    ExecutorService recvExecutorService = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    ExecutorService workerExecutorService = new ThreadPoolExecutor(1, 1,
            0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<>());

    LinkedBlockingQueue<Order> queue = new LinkedBlockingQueue<>();

    EventLoopGroup group;
    Bootstrap bootstrap;
    ObjectMapper objectMapper = new ObjectMapper();
    Channel channel;
    String ip;
    int port;
    Properties properties;
    ChromeDriver driver;

    /**
     * just for unit test
     */
    public BizAssistant() {
        initDriver();
    }

    public BizAssistant(Properties properties) {
        initDriver();
        this.ip = properties.getProperty("ip");
        this.port = Integer.valueOf(properties.getProperty("port"));
        this.properties = properties;

        recvExecutorService.submit(
                new Thread("recv-thread") {
                    @Override
                    public void run() {
                        group = new NioEventLoopGroup();
                        bootstrap = new Bootstrap();
                        bootstrap.group(group)
                                .channel(NioSocketChannel.class)
                                .handler(new ChannelInitializer<Channel>() {
                                    @Override
                                    protected void initChannel(Channel ch) throws Exception {
                                        ch.pipeline().addLast(new IdleStateHandler(Integer.valueOf(properties.getProperty("read.timeout.second")), 0, 0));
                                        ch.pipeline().addLast(new SimpleChannelInboundHandler<String>() {
                                            @Override
                                            protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
                                                queue.offer(objectMapper.readValue(msg, Order.class));
                                            }
                                        });
                                    }
                                });
                    }
                }
        );

        for (Integer i = 0; i < Integer.valueOf(properties.getProperty("worker.num")); i++) {
            workerExecutorService.submit(new Worker(i, queue, driver, properties));
        }
    }

    private void initDriver() {
        System.setProperty("webdriver.chrome.driver", ClassLoader.getSystemResource("chromedriver.exe").getFile());
        ChromeOptions options = new ChromeOptions();
        options.addArguments(new String[]{"--start-maximized"});
        options.setCapability("acceptSslCerts", true);
        options.setCapability("acceptInsecureCerts", true);
        this.driver = new ChromeDriver(options);
    }


    Channel getChannel() {
        boolean channelNotValid = this.channel == null || (this.channel != null && !this.channel.isActive());
        if (channelNotValid) {
            ChannelFuture channelFuture = bootstrap.connect(ip, port).syncUninterruptibly();
            this.channel = channelFuture.channel();
        }
        return channel;
    }

    public void shutdown() {
        group.shutdownGracefully();
        recvExecutorService.shutdown();
        workerExecutorService.shutdown();
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
                    getChannel().writeAndFlush(rst);
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
        Properties properties = ConfigProperties.load();

        //init
        BizAssistant bizAssistant = new BizAssistant(properties);

        Runtime.getRuntime().addShutdownHook(new Thread(() -> bizAssistant.shutdown()));
    }



}
