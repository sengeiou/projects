package com.normal.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

/**
 * @author: fei.he
 */
public class Digests {
    public static final Logger logger = LoggerFactory.getLogger(Digests.class);

    static SecretKeySpec key;
    static Cipher cipher;

    static {
        try {
            String seed = "H9F*$_xiW~";
            KeyGenerator keyGen = KeyGenerator.getInstance("AES");
            SecureRandom random = SecureRandom.getInstance("SHA1PRNG");
            random.setSeed(seed.getBytes());
            keyGen.init(128, random);
            SecretKey secretKey = keyGen.generateKey();
            key = new SecretKeySpec(secretKey.getEncoded(), "AES");
            cipher = Cipher.getInstance("AES");
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static String md5(String input) {
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(input.getBytes());
        byte[] digest = md.digest();
        return DatatypeConverter.printHexBinary(digest).toUpperCase();
    }


    /**
     * 将二进制转换成16进制
     *
     * @param buf
     * @return
     */
    private static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
            String hex = Integer.toHexString(buf[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            sb.append(hex.toUpperCase());
        }
        return sb.toString();
    }

    /**
     * 将16进制转换为二进制
     *
     * @param hexStr
     * @return
     */
    private static byte[] parseHexStr2Byte(String hexStr) {
        if (hexStr.length() < 1) {
            return null;
        }
        byte[] result = new byte[hexStr.length() / 2];
        for (int i = 0; i < hexStr.length() / 2; i++) {
            int high = Integer.parseInt(hexStr.substring(i * 2, i * 2 + 1), 16);
            int low = Integer.parseInt(hexStr.substring(i * 2 + 1, i * 2 + 2), 16);
            result[i] = (byte) (high * 16 + low);
        }
        return result;
    }

    public static String encrypt(String raw) {
        try {
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return parseByte2HexStr(cipher.doFinal(raw.getBytes()));
        } catch (Exception e) {
            logger.error("e:{}", e);
        }
        throw new RuntimeException("加密失败");
    }

    public static String decrypt(String decryptStr) {
        try {
            cipher.init(Cipher.DECRYPT_MODE, key);
            return new String(cipher.doFinal(parseHexStr2Byte(decryptStr)));
        } catch (Exception e) {
            logger.error("e:{}", e);
        }
        throw new RuntimeException("解密失败");
    }
}
