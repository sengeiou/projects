package com.normal.portal;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("post")
public class PostConfig {
    /**
     * 相对于上下文路径
     */
    private String markdownRelativePosition;

    public String getMarkdownRelativePosition() {
        return markdownRelativePosition;
    }

    public void setMarkdownRelativePosition(String markdownRelativePosition) {
        this.markdownRelativePosition = markdownRelativePosition;
    }


}
