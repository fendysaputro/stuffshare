package com.stuff.stuffshare.model;

import java.io.Serializable;

public class CampaignCategory implements Serializable {

    String id;
    String category;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}
