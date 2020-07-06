package com.stuff.stuffshare;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;

import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.Data;
import com.stuff.stuffshare.model.Donation;
import com.stuff.stuffshare.model.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;

public class StuffShareApp extends Application {
    public static String HOST = "http://stuffshare.warnalangit.com/";
    public static String LOGIN_PATH = "api/auth";
    public static String REGISTER_PATH = "api/user/registration";
    public static String AKUN_PLUS_REGISTER_PATH = "api/akunplus/registration";
    public static String ADD_CAMPAIGN = "api/penggalangan/add";
    public static String CATEGORY = "api/kategoribarang";
    public static String CAMPAIGN = "api/penggalangan/status/1";
    public static String CATEGORY_CAMPAIGN = "api/kategoripenggalangan";
    public static String CATEGORY_BARANG = "api/kategoribarang";
    public static String DONATION = "api/donasi/user/";
    public static String ADD_DONATION = "api/donasi/add";
    public static String BANK = "api/bank";
    public static String CHANGE_PASSWORD = "api/rubahpassword";
    public static String EDIT_PHOTO = "api/user/edit-photo";
    public static String CONFIRMATION_DONATION = "api/donasi/konfirmasi";
    public static String FORGET_PASSWORD = "api/lupapassword";
    public static String GET_USER = "api/user/";
    public static String ALL_CAMPAIGN = "api/penggalangan/user/";
    public int imgId;
    public String imgCampaign;
    boolean login;
    User user;
    public static final String TAG = "StuffShare";
    public String countDonation, nominalDonation, messageDonation;
    public int imageSchedule;
    public String nameCommunity, addressCommunity, numberAktaCommunity, nameKetuaCommunity, npwpKetua;
    private Bitmap akta, npwp, imageOrg, imageUpload, imgProfile, imgConfirmation, resiConfirmation, imgKosong;
    File imageFileDoc;
    Campaigner campaigner;
    boolean akunPlus;
    Bank bank;
    private Campaigner selectedCampaigner;
    private Donation selectedDonation;
    String jmlCount, cerita;
    String catOne, catTwo, catThree;
    String kategori, penerima, phoneReceiver, accident, addressReceiver, dateAccident, periode, titleCampaign, needDonation, forWhat;
    String image_community;
    boolean picture;
    public static int notificationId = 1;
    Data data;
    boolean notification;

    private ArrayList<CategoryBarang> CategoryBarangs = new ArrayList<CategoryBarang>();

    public ArrayList<CategoryBarang> getCategoryBarangs() {
        return CategoryBarangs;
    }

    public void setCategoryBarangs(ArrayList<CategoryBarang> categoryBarangs) {
        CategoryBarangs = categoryBarangs;
    }

    public int getImgId() {
        return imgId;
    }

    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public String getImgCampaign() {
        return imgCampaign;
    }

    public void setImgCampaign(String imgCampaign) {
        this.imgCampaign = imgCampaign;
    }

    public boolean isLogin(Activity fa, int MODE) {
        SharedPreferences sharedPref = fa.getPreferences(MODE);
        String userString = sharedPref.getString("user", "");
        if(userString.isEmpty()){
            login = false;
        } else {
            login = true;
            try {
                JSONObject userObj = new JSONObject(userString);
                user = new User(userObj.getString("id"),
                        userObj.getString("name"), userObj.getString("phone"),
                        userObj.getString("email"), userObj.getInt("akunplus"),
                        userObj.getString("token"), userObj.getString("image"),
                        userObj.getString("status_akunplus"));
            } catch (JSONException e){
                e.printStackTrace();
            }
        }
        return login;
    }

    public void setLogin(boolean login) {
        this.login = login;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCountDonation() {
        return countDonation;
    }

    public void setCountDonation(String countDonation) {
        this.countDonation = countDonation;
    }

    public int getImageSchedule() {
        return imageSchedule;
    }

    public void setImageSchedule(int imageSchedule) {
        this.imageSchedule = imageSchedule;
    }

    public String getNameCommunity() {
        return nameCommunity;
    }

    public void setNameCommunity(String nameCommunity) {
        this.nameCommunity = nameCommunity;
    }

    public String getAddressCommunity() {
        return addressCommunity;
    }

    public void setAddressCommunity(String addressCommunity) {
        this.addressCommunity = addressCommunity;
    }

    public String getNumberAktaCommunity() {
        return numberAktaCommunity;
    }

    public void setNumberAktaCommunity(String numberAktaCommunity) {
        this.numberAktaCommunity = numberAktaCommunity;
    }

    public String getNameKetuaCommunity() {
        return nameKetuaCommunity;
    }

    public void setNameKetuaCommunity(String nameKetuaCommunity) {
        this.nameKetuaCommunity = nameKetuaCommunity;
    }

    public String getNpwpKetua() {
        return npwpKetua;
    }

    public void setNpwpKetua(String npwpKetua) {
        this.npwpKetua = npwpKetua;
    }

    public Bitmap getAkta() {
        return akta;
    }

    public void setAkta(Bitmap akta) {
        this.akta = akta;
    }

    public Bitmap getNpwp() {
        return npwp;
    }

    public void setNpwp(Bitmap npwp) {
        this.npwp = npwp;
    }

    public Bitmap getImageOrg() {
        return imageOrg;
    }

    public void setImageOrg(Bitmap imageOrg) {
        this.imageOrg = imageOrg;
    }

    public Bitmap getImageUpload() {
        return imageUpload;
    }

    public void setImageUpload(Bitmap imageUpload) {
        this.imageUpload = imageUpload;
    }

    public File getImageFileDoc() {
        return imageFileDoc;
    }

    public void setImageFileDoc(File imageFileDoc) {
        this.imageFileDoc = imageFileDoc;
    }

    public Bitmap getImgConfirmation() {
        return imgConfirmation;
    }

    public void setImgConfirmation(Bitmap imgConfirmation) {
        this.imgConfirmation = imgConfirmation;
    }

    public Campaigner getCampaigner() {
        return campaigner;
    }

    public void setCampaigner(Campaigner campaigner) {
        this.campaigner = campaigner;
    }

    public boolean isAkunPlus() {
        return akunPlus;
    }

    public void setAkunPlus(boolean akunPlus) {
        this.akunPlus = akunPlus;
    }

    public Bank getBank() {
        return bank;
    }

    public void setBank(Bank bank) {
        this.bank = bank;
    }

    public String getNominalDonation() {
        return nominalDonation;
    }

    public void setNominalDonation(String nominalDonation) {
        this.nominalDonation = nominalDonation;
    }

    public Campaigner getSelectedCampaigner() {
        return selectedCampaigner;
    }

    public void setSelectedCampaigner(Campaigner selectedCampaigner) {
        this.selectedCampaigner = selectedCampaigner;
    }

    public String getJmlCount() {
        return jmlCount;
    }

    public void setJmlCount(String jmlCount) {
        this.jmlCount = jmlCount;
    }

    public String getCerita() {
        return cerita;
    }

    public void setCerita(String cerita) {
        this.cerita = cerita;
    }

    public String getCatOne() {
        return catOne;
    }

    public void setCatOne(String catOne) {
        this.catOne = catOne;
    }

    public String getCatTwo() {
        return catTwo;
    }

    public void setCatTwo(String catTwo) {
        this.catTwo = catTwo;
    }

    public String getCatThree() {
        return catThree;
    }

    public void setCatThree(String catThree) {
        this.catThree = catThree;
    }

    public Bitmap getImgProfile() {
        return imgProfile;
    }

    public void setImgProfile(Bitmap imgProfile) {
        this.imgProfile = imgProfile;
    }

    public String getPenerima() {
        return penerima;
    }

    public void setPenerima(String penerima) {
        this.penerima = penerima;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
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

    public String getPeriode() {
        return periode;
    }

    public void setPeriode(String periode) {
        this.periode = periode;
    }

    public String getTitleCampaign() {
        return titleCampaign;
    }

    public void setTitleCampaign(String titleCampaign) {
        this.titleCampaign = titleCampaign;
    }

    public String getNeedDonation() {
        return needDonation;
    }

    public void setNeedDonation(String needDonation) {
        this.needDonation = needDonation;
    }

    public String getForWhat() {
        return forWhat;
    }

    public void setForWhat(String forWhat) {
        this.forWhat = forWhat;
    }

    public Donation getSelectedDonation() {
        return selectedDonation;
    }

    public void setSelectedDonation(Donation selectedDonation) {
        this.selectedDonation = selectedDonation;
    }

    public String getImage_community() {
        return image_community;
    }

    public void setImage_community(String image_community) {
        this.image_community = image_community;
    }

    public boolean isPicture() {
        return picture;
    }

    public void setPicture(boolean picture) {
        this.picture = picture;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

    public boolean isNotification() {
        return notification;
    }

    public void setNotification(boolean notification) {
        this.notification = notification;
    }

    public String getMessageDonation() {
        return messageDonation;
    }

    public void setMessageDonation(String messageDonation) {
        this.messageDonation = messageDonation;
    }

    public Bitmap getResiConfirmation() {
        return resiConfirmation;
    }

    public void setResiConfirmation(Bitmap resiConfirmation) {
        this.resiConfirmation = resiConfirmation;
    }

    public Bitmap getImgKosong() {
        return imgKosong;
    }

    public void setImgKosong(Bitmap imgKosong) {
        this.imgKosong = imgKosong;
    }
}
