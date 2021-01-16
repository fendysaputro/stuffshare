package com.stuff.stuffshare.model;

import org.json.JSONArray;

/**
 * Created by phephen on 16,January,2021
 * https://github.com/fendysaputro
 */
public class DonasiBayar {
    String id;
    String tglDonasi;
    String nmDonasi;
    JSONArray totalDonasiBarang;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTglDonasi() {
        return tglDonasi;
    }

    public void setTglDonasi(String tglDonasi) {
        this.tglDonasi = tglDonasi;
    }

    public String getNmDonasi() {
        return nmDonasi;
    }

    public void setNmDonasi(String nmDonasi) {
        this.nmDonasi = nmDonasi;
    }

    public JSONArray getTotalDonasiBarang() {
        return totalDonasiBarang;
    }

    public void setTotalDonasiBarang(JSONArray totalDonasiBarang) {
        this.totalDonasiBarang = totalDonasiBarang;
    }
}
