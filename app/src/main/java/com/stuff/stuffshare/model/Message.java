package com.stuff.stuffshare.model;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by Fendy Saputro on 21/11/2020.
 * vidis194@gmail.com
 */
public class Message extends RecyclerView.ViewHolder {
    String message;
    String date;

    public Message(View itemView) {
        super(itemView);
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
