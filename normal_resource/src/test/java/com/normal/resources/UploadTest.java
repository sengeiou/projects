package com.normal.resources;

import com.squareup.okhttp.*;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

public class UploadTest {

    private final OkHttpClient client = new OkHttpClient();
    public static final MediaType MEDIA_TYPE_MARKDOWN
            = MediaType.parse("multipart/form-data; boundary=----WebKitFormBoundarysQUwHj6fZ7nojhpZ");


    @Test
    public void testUpload() throws IOException {
        File file = new File("D:\\projects\\normal_resource\\src\\test\\java\\com\\normal\\resources\\upload.class");
        Request request = new Request.Builder()
                .url("https://upload.ctfile.com/web/upload.do?userid=18565377&maxsize=2147483648&folderid=0&ctt=1594718137&limit=2&spd=23000000&key=40fbc81a8307c5a1ef7cd346d1b71213")
                .addHeader(":authority", "upload.ctfile.com")
                .addHeader(":method", "POST")
                .addHeader(":path", "/web/upload.do?userid=18565377&maxsize=2147483648&folderid=0&ctt=1594718137&limit=2&spd=23000000&key=40fbc81a8307c5a1ef7cd346d1b71213")
                .addHeader(":scheme", "https")
                .addHeader("accept", "*/*")
                .addHeader("accept-encoding", "gzip, deflate, br")
                .addHeader("cache-control", "no-cache")
                .addHeader("content-length", String.valueOf(file.length()))
                .addHeader("content-type", "multipart/form-data; boundary=----WebKitFormBoundarysQUwHj6fZ7nojhpZ")
                .addHeader("origin", "https://home.400gb.com")
                .addHeader("pragma", "no-cache")
                .addHeader("referer", "https://home.400gb.com/")
                .addHeader("sec-fetch-dest", "empty")
                .addHeader("sec-fetch-mode", "cors")
                .addHeader("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.116 Safari/537.36")
                .post(RequestBody.create(MEDIA_TYPE_MARKDOWN, file))
                .build();

        Response response = client.newCall(request).execute();
        if (!response.isSuccessful()) {
            System.out.println("fail: " + response.toString());
        }
        System.out.println(response);
    }

}

