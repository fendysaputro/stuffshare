package com.stuff.stuffshare.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UploadConfirmationTask extends AsyncTask<Object, Void, String> {

    OnHttpResponseListener onHttpResponseListener;

    public void setOnHttpResponseListener(OnHttpResponseListener listener) {
        onHttpResponseListener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        String userid = (String) params[1];
        String id_donasi = (String) params[2];
        String id_bank = (String) params[3];
        String metode = (String) params[4];
        String nama_pengirim = (String) params[5];
        String bank_pengirim = (String) params[6];
        String norek = (String) params[7];
        String jumlahbayar = (String) params[8];
        Bitmap gambar = (Bitmap) params[9];

        ByteArrayOutputStream gambarBoas = new ByteArrayOutputStream();
        gambar.compress(Bitmap.CompressFormat.PNG, 1, gambarBoas);
        String data = "";

        try {
            HttpMultipartClient client = new HttpMultipartClient(url);
            client.connectForMultipart();
            client.addFormPart("userid", userid);
            client.addFormPart("id_donasi", id_donasi);
            client.addFormPart("id_bank", id_bank);
            client.addFormPart("metode", metode);
            client.addFormPart("nm_pengirim", nama_pengirim);
            client.addFormPart("bank_pengirim", bank_pengirim);
            client.addFormPart("norek", norek);
            client.addFormPart("jumlahbayar", jumlahbayar);
            client.addFilePart("gambar", "png", gambarBoas.toByteArray());

            client.finishMultipart();
            data = client.getResponse();
            Log.i("player-client", client.toString());
        } catch (Throwable throwable){
            throwable.printStackTrace();
        }
        return data;
    }

    @Override
    protected void onPostExecute(String result) {
        if (onHttpResponseListener != null){
            onHttpResponseListener.OnHttpResponse(result);
        }
    }
}
