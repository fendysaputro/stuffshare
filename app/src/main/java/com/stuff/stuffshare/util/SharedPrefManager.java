package com.stuff.stuffshare.util;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefManager {

    public static final String STUFFSHAREAPP = "stuffshareApp";

    public static String name = "name";
    public static String email = "email";
    public static String phone = "phone";
    public static String userid = "userid";
    public static int akunplus = 0;
    public static String image = "image";
    public static String image_community = "imageCom";
    public static String target = "target";
    public static String kategori = "kategori";
    public static String penerima = "penerima";
    public static String nohp = "nohp";
    public static String alamat_penerima = "alamat_penerima";
    public static String kejadian = "kejadian";
    public static String tglkejadian = "tglkejadian";
    public static String judul = "judul";
    public static String kebutuhan_dana = "kebutuhan_dana";
    public static String digunakanuntuk = "digunakanuntuk";
    public static String cerita = "cerita";
    public static String periode = "periode";
    public static String buku = "buku";
    public static String elektronik = "elektronik";
    public static String sepatu = "sepatu";
    public static String alamat_penyelenggara = "alamat_penyelenggara";

    public static final String login = "login";
    public static final String accountPlus ="accountPlus";
    public static final String notif = "notif";
    public static final String statusAccountPlus = "status_akunplus";

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    public SharedPrefManager(Context context){
        sharedPreferences = context.getSharedPreferences(STUFFSHAREAPP, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSPString(String keySP, String value){
        editor.putString(keySP, value);
        editor.commit();
    }

    public void saveSPInt(String keySP, int value){
        editor.putInt(keySP, value);
        editor.commit();
    }

    public void saveSPBoolean(String keySP, boolean value){
        editor.putBoolean(keySP, value);
        editor.commit();
    }

    public String getSPName(){
        return sharedPreferences.getString(name, "");
    }

    public String getSPEmail(){
        return sharedPreferences.getString(email, "");
    }

    public String getSPPhone(){
        return sharedPreferences.getString(phone, "");
    }

    public String getSPUserid(){
        return sharedPreferences.getString(userid, "");
    }

    public int getSPAkunplus() {
        return sharedPreferences.getInt("akunplus", 0);
    }

    public String getSPImage() {
        return sharedPreferences.getString(image, "");
    }

    public String getSPImageCom() {
        return sharedPreferences.getString(image_community, "");
    }

    public String getSPKategori() {
        return sharedPreferences.getString(kategori, "");
    }

    public String getSPPenerima() {
        return sharedPreferences.getString(penerima, "");
    }

    public String getSPNoHp() {
        return sharedPreferences.getString(nohp, "");
    }

    public String getSPAlamatPenerima() {
        return sharedPreferences.getString(alamat_penerima, "");
    }

    public String getSPKejadian() {
        return sharedPreferences.getString(kejadian, "");
    }

    public String getSPTglKejadian() {
        return sharedPreferences.getString(tglkejadian, "");
    }

    public String getSPJudul() {
        return sharedPreferences.getString(judul, "");
    }

    public String getSPKebutuhanDana() {
        return sharedPreferences.getString(kebutuhan_dana, "");
    }

    public String getSPDigunakanuntuk() {
        return sharedPreferences.getString(digunakanuntuk, "");
    }

    public String getSPCerita() {
        return sharedPreferences.getString(cerita, "");
    }

    public String getSPPeriode() {
        return sharedPreferences.getString(periode, "");
    }

    public String getSPTarget() {
        return sharedPreferences.getString(target, "");
    }

    public String getSPBuku() {
        return sharedPreferences.getString(buku, "");
    }

    public String getSPSepatu() {
        return sharedPreferences.getString(sepatu, "");
    }

    public String getSPElektronik() {
        return sharedPreferences.getString(elektronik, "");
    }

    public Boolean getSPSudahLogin(){
        return sharedPreferences.getBoolean(login, false);
    }

    public Boolean getSPAccountPlus() {
        return sharedPreferences.getBoolean(accountPlus, false);
    }

    public Boolean getSPNotif () {
        return sharedPreferences.getBoolean(notif, false);
    }

    public String getSPAlamatPenyelenggara() {
        return sharedPreferences.getString(alamat_penyelenggara, "");
    }

    public String getSPStatusAkunPlus() {
        return sharedPreferences.getString(statusAccountPlus, "");
    }
}
