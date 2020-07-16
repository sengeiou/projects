package com.normal.portal.impl;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("post")
public class PostProperties {
    public static final String token = "Hefei123$%^";

    private String uploadDir;

    public String getUploadDir() {
        return uploadDir;
    }

    public void setUploadDir(String uploadDir) {
        this.uploadDir = uploadDir;
    }
}
