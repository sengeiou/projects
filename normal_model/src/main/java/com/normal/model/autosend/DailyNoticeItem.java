package com.normal.model.autosend;



/**
 * @author: fei.he
 */
public class DailyNoticeItem {

    /**
     * 回车分隔
     */
    private String text;


    private String imagePath;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }
}
