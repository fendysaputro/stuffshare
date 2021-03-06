package com.stuff.stuffshare.model;

public class User {
    String id;
    String name;
    String phone;
    String email;
    int akunplus;
    String token;
    String image;
    String status_akunplus;

    public User(String id, String name, String phone, String email, int akunplus, String token, String image, String status_akunplus){
        this.id = id;
        this.name = name;
        this.phone = phone;
        this.email = email;
        this.akunplus = akunplus;
        this.token = token;
        this.image = image;
        this.status_akunplus = status_akunplus;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAkunplus() {
        return akunplus;
    }

    public void setAkunplus(int akunplus) {
        this.akunplus = akunplus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getStatus_akunplus() {
        return status_akunplus;
    }

    public void setStatus_akunplus(String status_akunplus) {
        this.status_akunplus = status_akunplus;
    }
}
