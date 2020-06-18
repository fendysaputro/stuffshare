package com.stuff.stuffshare.model;

public class RowItem {

    private int imageId;
    private String desc;
    private String days;

    public RowItem(int imageId, String desc, String days) {
        this.imageId = imageId;
        this.desc = desc;
        this.days = days;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }
}
