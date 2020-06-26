package com.stuff.stuffshare.model;

import org.json.JSONArray;

public class Donation {
    String id;
    String userId;
    String penggalangan;
    String nmtampilan;
    String gambar;
    String date;
    String donasiUang;
    String bankName;
    String metodeKirim;
    String metodeBayar;
    JSONArray donasiBarang;
    String alamatPenyelenggara;
    String status;
    int totalDonation;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPenggalangan() {
        return penggalangan;
    }

    public void setPenggalangan(String penggalangan) {
        this.penggalangan = penggalangan;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNmtampilan() {
        return nmtampilan;
    }

    public void setNmtampilan(String nmtampilan) {
        this.nmtampilan = nmtampilan;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getDonasiUang() {
        return donasiUang;
    }

    public void setDonasiUang(String donasiUang) {
        this.donasiUang = donasiUang;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getMetodeKirim() {
        return metodeKirim;
    }

    public void setMetodeKirim(String metodeKirim) {
        this.metodeKirim = metodeKirim;
    }

    public String getMetodeBayar() {
        return metodeBayar;
    }

    public void setMetodeBayar(String metodeBayar) {
        this.metodeBayar = metodeBayar;
    }

    public JSONArray getDonasiBarang() {
        return donasiBarang;
    }

    public void setDonasiBarang(JSONArray donasiBarang) {
        this.donasiBarang = donasiBarang;
    }

    public String getAlamatPenyelenggara() {
        return alamatPenyelenggara;
    }

    public void setAlamatPenyelenggara(String alamatPenyelenggara) {
        this.alamatPenyelenggara = alamatPenyelenggara;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalDonation() {
        return totalDonation;
    }

    public void setTotalDonation(int totalDonation) {
        this.totalDonation = totalDonation;
    }
}
