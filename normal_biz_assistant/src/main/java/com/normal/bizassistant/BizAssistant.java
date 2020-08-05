package com.normal.bizassistant;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.normal.bizmodel.Order;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

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


    public BizAssistant(Properties properties) {
        System.setProperty("webdriver.chrome.driver",ClassLoader.getSystemResource("chromedriver.exe").getFile() );
        ChromeOptions options = new ChromeOptions();
        options.addArguments(new String[]{"--start-maximized"});
        options.setCapability("acceptSslCerts", true);
        options.setCapability("acceptInsecureCerts", true);
        ChromeDriver driver = new ChromeDriver();

        this.ip = properties.getProperty("ip");
        this.port = Integer.valueOf(properties.getProperty("port"));

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
            workerExecutorService.submit(new Worker(i, queue, driver));
        }
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

    static class Worker extends Thread {

        LinkedBlockingQueue<Order> queue;

        ChromeDriver driver;


        public Worker(int i, LinkedBlockingQueue<Order> queue, ChromeDriver driver) {
            this.setName("worker-thread" + i);
            this.queue = queue;
            this.driver = driver;
        }

        @Override
        public void run() {
            for (; ; ) {
                if (Thread.currentThread().isInterrupted()) {
                    logger.info("worker thread had been interrupted return");
                    break;
                }
                try {
                    queryPaidUntilTimeout(queue.take());
                } catch (InterruptedException e) {
                    //clear interrupted status
                    Thread.interrupted();
                }
            }
        }

        private boolean queryPaidUntilTimeout(Order order) {
            driver.executeScript("alert('ok')");
            return false;
        }
    }

    public static void main(String[] args) throws Exception {
        Properties properties = new Properties();
        InputStream input = ClassLoader.getSystemResourceAsStream("application.properties");
        properties.load(input);
        input.close();

        BizAssistant bizAssistant = new BizAssistant(properties);
        Runtime.getRuntime().addShutdownHook(new Thread(() -> bizAssistant.shutdown()));
    }


}
