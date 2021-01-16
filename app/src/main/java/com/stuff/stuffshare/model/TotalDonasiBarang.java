package com.stuff.stuffshare.model;

/**
 * Created by phephen on 16,January,2021
 * https://github.com/fendysaputro
 */
public class TotalDonasiBarang {
    String id;
    String barangDonasi;
    String url;
    String qty;
    String qtyTotal;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getBarangDonasi() {
        return barangDonasi;
    }

    public void setBarangDonasi(String barangDonasi) {
        this.barangDonasi = barangDonasi;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getQtyTotal() {
        return qtyTotal;
    }

    public void setQtyTotal(String qtyTotal) {
        this.qtyTotal = qtyTotal;
    }
}
