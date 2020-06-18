package com.stuff.stuffshare.network;

import com.google.gson.annotations.SerializedName;

public class ServerResponse {

    @SerializedName("r")
    public boolean r;
    @SerializedName("m")
    public String m;

    public String getM() {
        return m;
    }

    public boolean getR(){
        return r;
    }
}
