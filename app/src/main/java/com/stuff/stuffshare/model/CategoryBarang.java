package com.stuff.stuffshare.model;

import java.io.Serializable;

public class CategoryBarang implements Serializable {

    private String imageId;
    private String id;
    private String productName;
    private String count;
    private String address;
    private String penerimaan;

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPenerimaan() {
        return penerimaan;
    }

    public void setPenerimaan(String penerimaan) {
        this.penerimaan = penerimaan;
    }
}
