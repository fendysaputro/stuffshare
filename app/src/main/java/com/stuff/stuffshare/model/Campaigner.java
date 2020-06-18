package com.stuff.stuffshare.model;

import org.json.JSONArray;

import java.util.Date;

public class Campaigner {
    String id;
    String userId;
    String targetDonation; // kategori
    String nameReceiver;
    String phoneReceiver;
    String accident;
    String addressReceiver;
    String dateAccident;
    String titleCampaign;
    String dana;
    String shirt;
    String book;
    String electronic;
    String music;
    String shoes;
    String forWhat;
    String periode;
    String story;
    String imageCampaign;
    String desc;
    String tglBuat;
    String tglSelesai;
    String masaDonasi, sisaHari, statusCampaign;
    JSONArray donasiBarang;
    String organization, countDonation, alamatPenyelenggara;
    String imageCom;

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

    public String getTargetDonation() {
        return targetDonation;
    }

    public void setTargetDonation(String targetDonation) {
        this.targetDonation = targetDonation;
    }

    public String getNameReceiver() {
        return nameReceiver;
    }

    public void setNameReceiver(String nameReceiver) {
        this.nameReceiver = nameReceiver;
    }

    public String getPhoneReceiver() {
        return phoneReceiver;
    }

    public void setPhoneReceiver(String phoneReceiver) {
        this.phoneReceiver = phoneReceiver;
    }

    public String getAccident() {
        return accident;
    }

    public void setAccident(String accident) {
        this.accident = accident;
    }

    public String getAddressReceiver() {
        return addressReceiver;
    }

    public void setAddressReceiver(String addressReceiver) {
        this.addressReceiver = addressReceiver;
    }

    public String getDateAccident() {
        return dateAccident;
    }

    public void setDateAccident(String dateAccident) {
        this.dateAccident = dateAccident;
    }

    public String getTitleCampaign() {
        return titleCampaign;
    }

    public void setTitleCampaign(String titleCampaign) {
        this.titleCampaign = titleCampaign;
    }

    public String getDana() {
        return dana;
    }

    public void setDana(String dana) {
        this.dana = dana;
    }

    public String getShirt() {
        return shirt;
    }

    public void setShirt(String shirt) {
        this.shirt = shirt;
    }

    public String getBook() {
        return book;
    }

    public void setBook(String book) {
        this.book = book;
    }

    public String getElectronic() {
        return electronic;
    }

    public void setElectronic(String electronic) {
        this.electronic = electronic;
    }

    public String getMusic() {
        return music;
    }

    public void setMusic(String music) {
        this.music = music;
    }

    public String getShoes() {
        return shoes;
    }

    public void setShoes(String shoes) {
        this.shoes = shoes;
    }

    public String getForWhat() {
        return forWhat;
    }

    public void setForWhat(String forWhat) {
        this.forWhat = forWhat;
    }

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getStory() {
        return story;
    }

    public void setStory(String story) {
        this.story = story;
    }

    public String getImageCampaign() {
        return imageCampaign;
    }

    public void setImageCampaign(String imageCampaign) {
        this.imageCampaign = imageCampaign;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getTglBuat() {
        return tglBuat;
    }

    public void setTglBuat(String tglBuat) {
        this.tglBuat = tglBuat;
    }

    public String getTglSelesai() {
        return tglSelesai;
    }

    public void setTglSelesai(String tglSelesai) {
        this.tglSelesai = tglSelesai;
    }

    public String getMasaDonasi() {
        return masaDonasi;
    }

    public void setMasaDonasi(String masaDonasi) {
        this.masaDonasi = masaDonasi;
    }

    public String getSisaHari() {
        return sisaHari;
    }

    public void setSisaHari(String sisaHari) {
        this.sisaHari = sisaHari;
    }

    public JSONArray getDonasiBarang() {
        return donasiBarang;
    }

    public void setDonasiBarang(JSONArray donasiBarang) {
        this.donasiBarang = donasiBarang;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public String getCountDonation() {
        return countDonation;
    }

    public void setCountDonation(String countDonation) {
        this.countDonation = countDonation;
    }

    public String getAlamatPenyelenggara() {
        return alamatPenyelenggara;
    }

    public void setAlamatPenyelenggara(String alamatPenyelenggara) {
        this.alamatPenyelenggara = alamatPenyelenggara;
    }

    public String getStatusCampaign() {
        return statusCampaign;
    }

    public void setStatusCampaign(String statusCampaign) {
        this.statusCampaign = statusCampaign;
    }

    public String getImageCom() {
        return imageCom;
    }

    public void setImageCom(String imageCom) {
        this.imageCom = imageCom;
    }
}
