package com.normal.base.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * @author: fei.he
 */
public class Digests {
    public static final Logger logger = LoggerFactory.getLogger(Digests.class);

    final static String secretKey = "q0vhqIMtNtyT2ecXieB34g==";

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


    public static String aesEncode(String value) {
        for (int i = 0; i < 3; i++) {
            byte[] encode = Base64.getUrlEncoder().encode((value + secretKey).getBytes());
            value = new String(encode);
        }
        return value;
    }

    public static String aesDecode(String value) {
        for (int i = 0; i < 3; i++) {
            byte[] encode = Base64.getUrlDecoder().decode(value.getBytes());
            value = new String(encode);
        }
        return value;
    }

    public static void main(String[] args) throws Exception {
      /*  SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
// get base64 encoded version of the key
        String encodedKey = Base64.getEncoder().encodeToString(secretKey.getEncoded());
        System.out.println(encodedKey);
        //q0vhqIMtNtyT2ecXieB34g==*/

        String encode = aesEncode("name=hefei;phone=17816872538");
        String decode = aesDecode(encode);
        System.out.println(encode + "\n " + decode);
    }
}
