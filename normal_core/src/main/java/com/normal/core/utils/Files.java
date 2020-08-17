package com.normal.core.utils;

import java.io.*;
import java.net.URL;

public final class Files {


    public static byte[] url2Bytes(URL url) {
        InputStream input = null;
        ByteArrayOutputStream out = null;
        try {
            input = url.openStream();
            out = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while ((len = input.read(buffer)) != -1) {
                out.write(buffer, 0, len);
            }
            return out.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                if (input != null) {
                    input.close();
                }
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                //igonre
            }
        }
    }



    public static void download(URL url, String fileFullPath) {
        FileOutputStream output = null;
        try {
            File file = new File(fileFullPath);
            if (!file.exists()) {
                file.createNewFile();
            }
            output = new FileOutputStream(file);
            output.write(url2Bytes(url));
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {

                    throw new RuntimeException(e);
                }
            }
        }
    }


}
