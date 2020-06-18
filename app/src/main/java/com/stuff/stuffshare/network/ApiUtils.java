package com.stuff.stuffshare.network;

import android.content.Context;

import com.stuff.stuffshare.StuffShareApp;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static String BASE_URL = "http://stuffshare.warnalangit.com/";

    public static Retrofit getRetrofit(){
        return new Retrofit.Builder()
                .baseUrl(ApiUtils.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }
}
