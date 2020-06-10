package com.normal.portal.impl;

import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public final class PostHelper {

    public static final int maxLength = 200;

    public final static String getPostPreview(MultipartFile mdPost) throws IOException {
        InputStream input = mdPost.getInputStream();
        BufferedReader bufferReader = new BufferedReader(new InputStreamReader(input));
        StringBuffer preview = new StringBuffer();
        for (; ; ) {
            String line = bufferReader.readLine().trim();
            if (line.isEmpty()) {
                continue;
            }
            //first row
            int begIdx = line.indexOf(">");
            if (begIdx != -1) {
                preview.append(line.substring(begIdx + 1));
                continue;
            }
            if (line.indexOf("#") != -1) {
                break;
            }

            if (preview.length() > maxLength) {
                break;
            }
            preview.append(line);
        }
        return preview.toString();
    }


}
