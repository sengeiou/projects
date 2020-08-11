package com.normal.bizassistant;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;
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
        properties.put("js", jsStr);
        jsInput.close();
    }


    public static String getWebchatLocation() {
        return getProperty("autosend.webchat.location");
    }

    public static int getConnectTimeoutMillis() {
        return Integer.valueOf(getProperty("biz.connect.timeout.millis"));
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    public static int getClientWriteIdleSeconds() {
        return Integer.valueOf(getProperty("biz.client.write.timeout.seconds"));
    }

    public static String getBizClientConnectUri() {
        return getProperty("biz.client.wb.connect.uri");
    }

    public static List<String> getGroups() {
        return Arrays.asList(properties.getProperty("autosend.groups").split(","));
    }

    public static String getGoodPicsPath() {
        return properties.getProperty("autosend.goods.pic.path");
    }
}