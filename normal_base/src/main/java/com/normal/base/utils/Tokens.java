package com.normal.base.utils;

import com.normal.base.web.CommonErrorMsg;
import com.normal.base.web.Result;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;

/**
 * @author: fei.he
 */
public class Tokens {
    public static final Logger logger = LoggerFactory.getLogger(Tokens.class);

    private Map<String, String> payload = new HashMap<>();
    private Map<String, String> decryptPayload;

    private final static String expire = "expire";

    public Tokens() {
        long expireValue = Timestamp.valueOf(LocalDateTime.now().plusMonths(3L)).getTime();
        addPayload(expire, String.valueOf(expireValue));
    }

    /**
     * 生成token
     *
     * @return
     */
    public String gen() {
        StringJoiner joiner = new StringJoiner(";");
        for (Map.Entry<String, String> entry : this.payload.entrySet()) {
            joiner.add(entry.getKey() + ":" + entry.getValue());
        }
        return Digests.encrypt(joiner.toString());
    }

    public Result check(String token) {
        return check(token, null);
    }

    public Result check(String token, CallBack customCheck) {

        String raw = Digests.decrypt(token);
        for (String item : raw.split(";")) {
            String[] keyValue = item.split(":");
            //过期校验
            if (keyValue[0].equals(expire)) {
                long expire = Long.valueOf(keyValue[1]);
                LocalDateTime expireTime = LocalDateTime.ofInstant(new Timestamp(expire).toInstant(), ZoneId.systemDefault());
                if (LocalDateTime.now().isAfter(expireTime)) {
                    return Result.fail(CommonErrorMsg.AUTH_EXPIRE);
                }
            }
            //自定义校验
            if (customCheck != null) {
                Result rst = customCheck.check(keyValue);
                if (!rst.isSuccess()) {
                    return rst;
                }
            }
        }
        return Result.success();
    }

    public Map<String, String> getPayload(String token) {
        if (decryptPayload != null) {
            return decryptPayload;
        }
        String decrypt = Digests.decrypt(token);
        String[] items = decrypt.split(";");
        Map<String, String> rst = new HashMap<>(items.length);
        for (String item : items) {
            String[] keyValue = item.split(":");
            rst.put(keyValue[0], keyValue[1]);
        }
        this.decryptPayload = rst;
        return rst;
    }

    public String getPayloadItem(String token, String key) {
        return getPayload(token).get(key);
    }

    /**
     * 添加自定义内容
     *
     * @param key
     * @param value
     * @return
     */
    public Tokens addPayload(String key, String value) {
        this.payload.put(key, value);
        return this;
    }

    public interface CallBack {

        Result check(String[] keyValue);
    }

}
