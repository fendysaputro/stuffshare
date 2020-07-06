package com.stuff.stuffshare.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class UploadDonationTask extends AsyncTask<Object, Void, String> {

    OnHttpResponseListener onHttpResponseListener;

    public void setOnHttpResponseListener(OnHttpResponseListener listener) {
        onHttpResponseListener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        String userid = (String) params[1];
        String id_penggalangan = (String) params[2];
        String id_bank = (String) params[3];
        String donasi = (String) params[4];
        String alamat = (String) params[5];
        String nohp = (String) params[6];
        String metodekirim = (String) params[7];
        String metodebayar = (String) params[8];
        String pesan = (String) params[9];
        ArrayList<String> donasiBarangs = new ArrayList<String>();
        for (int i = 10; i < params.length; i++) {
            Log.i("stuffshare", "params " + params[i].toString());
            donasiBarangs.add((String) params[i]);
        }

        String data = "";

        try {
            HttpMultipartClient client = new HttpMultipartClient(url);
            client.connectForMultipart();
            client.addFormPart("userid", userid);
            client.addFormPart("id_penggalangan", id_penggalangan);
            client.addFormPart("id_bank", id_bank);
            client.addFormPart("donasi", donasi);
            client.addFormPart("alamat", alamat);
            client.addFormPart("nohp", nohp);
            client.addFormPart("metodekirim", metodekirim);
            client.addFormPart("metodebayar", metodebayar);
            client.addFormPart("pesan", pesan);
            for (int i = 0; i < donasiBarangs.size(); i++) {
                client.addFormPart("donasibarang[]", donasiBarangs.get(i));
            }
            client.finishMultipart();
            data = client.getResponse();
            Log.i("data-client", client.toString());
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
