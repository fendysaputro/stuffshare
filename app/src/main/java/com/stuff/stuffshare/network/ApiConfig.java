package com.stuff.stuffshare.network;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiConfig {
    @Multipart
    @POST("api/akunplus/registration")
    Call<ServerResponse> uploadMulFile(@Part MultipartBody.Part img_akta,
                                       @Part MultipartBody.Part img_npmwp,
                                       @Part MultipartBody.Part img_image);
}
