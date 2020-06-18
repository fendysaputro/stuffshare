package com.stuff.stuffshare.model;

public class CollectDonation {

    private int imageId;
    private String productName;
    private String count;

    public CollectDonation(int imageId, String productName) {
        this.imageId = imageId;
        this.productName = productName;
        this.count = count;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
