package com.stuff.stuffshare.model;

import java.util.Date;

public class ScheduleDonation {

    private int imagePenggalangId;
    private int iconCommunityId;
    private String sisaHari;
    private String status;
    private String penggalangName;
    private String masaDonasi;
    private Date scheduleDonasi;
    private String countDonasi;

    public ScheduleDonation(int imagePenggalangId, int iconCommunityId, String penggalangName) {
        this.imagePenggalangId = imagePenggalangId;
        this.iconCommunityId = iconCommunityId;
        this.penggalangName = penggalangName;
    }

    public int getImagePenggalangId() {
        return imagePenggalangId;
    }

    public void setImagePenggalangId(int imagePenggalangId) {
        this.imagePenggalangId = imagePenggalangId;
    }

    public int getIconCommunityId() {
        return iconCommunityId;
    }

    public void setIconCommunityId(int iconCommunityId) {
        this.iconCommunityId = iconCommunityId;
    }

    public String getSisaHari() {
        return sisaHari;
    }

    public void setSisaHari(String sisaHari) {
        this.sisaHari = sisaHari;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPenggalangName() {
        return penggalangName;
    }

    public void setPenggalangName(String penggalangName) {
        this.penggalangName = penggalangName;
    }

    public String getMasaDonasi() {
        return masaDonasi;
    }

    public void setMasaDonasi(String masaDonasi) {
        this.masaDonasi = masaDonasi;
    }

    public Date getScheduleDonasi() {
        return scheduleDonasi;
    }

    public void setScheduleDonasi(Date scheduleDonasi) {
        this.scheduleDonasi = scheduleDonasi;
    }

    public String getCountDonasi() {
        return countDonasi;
    }

    public void setCountDonasi(String countDonasi) {
        this.countDonasi = countDonasi;
    }
}
