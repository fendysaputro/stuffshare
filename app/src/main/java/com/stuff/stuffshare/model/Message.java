package com.stuff.stuffshare.model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import org.json.JSONArray;

/**
 * Created by Fendy Saputro on 21/11/2020.
 * vidis194@gmail.com
 */
public class Message {
    String userId;
    String name;
    String alamat;
    String phone;
    String status;
    int total_message;
    int total_unread;
    int total_read;
    JSONArray messageUser;
    String date;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAlamat() {
        return alamat;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotal_message() {
        return total_message;
    }

    public void setTotal_message(int total_message) {
        this.total_message = total_message;
    }

    public int getTotal_unread() {
        return total_unread;
    }

    public void setTotal_unread(int total_unread) {
        this.total_unread = total_unread;
    }

    public int getTotal_read() {
        return total_read;
    }

    public void setTotal_read(int total_read) {
        this.total_read = total_read;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public JSONArray getMessageUser() {
        return messageUser;
    }

    public void setMessageUser(JSONArray messageUser) {
        this.messageUser = messageUser;
    }
}
