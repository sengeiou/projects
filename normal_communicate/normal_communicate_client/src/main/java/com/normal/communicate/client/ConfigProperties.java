package com.normal.communicate.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * @author: fei.he
 */
public class ConfigProperties {

    private static Properties properties = new Properties();

    public static void load() throws IOException {
        properties.clear();
        InputStream propertiesInput = ClassLoader.getSystemResourceAsStream("application.properties");
        properties.load(propertiesInput);
        propertiesInput.close();

        InputStream jsInput = ClassLoader.getSystemResourceAsStream("index.js");
        BufferedReader bufReader = new BufferedReader(new InputStreamReader(jsInput));
        String jsStr = bufReader.lines().collect(Collectors.joining("\n"));
        properties.put("biz.client.order.js", jsStr);
        jsInput.close();
    }


    public static int getConnectTimeoutMillis() {
        return Integer.valueOf(getProperty("biz.client.connect.timeout.millis"));
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static String getBizClientConnectUri() {
        return getProperty("biz.client.wb.connect.uri");
    }



    public static String getClientConnectIp() {
        return properties.getProperty("biz.client.connect.ip");
    }

    public static int getClientConnectPort() {
        return Integer.valueOf(properties.getProperty("biz.client.connect.port"));
    }
}
