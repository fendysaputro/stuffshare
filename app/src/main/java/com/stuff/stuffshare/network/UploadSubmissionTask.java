package com.stuff.stuffshare.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.sql.Array;
import java.util.ArrayList;

public class UploadSubmissionTask extends AsyncTask<Object, Void, String> {

    OnHttpResponseListener onHttpResponseListener;

    public void setOnHttpResponseListener(OnHttpResponseListener listener) {
        onHttpResponseListener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        String userid = (String) params[1];
        String kategori = (String) params[2];
        String penerima = (String) params[3];
        String nohp = (String) params[4];
        String alamat_penerima = (String) params[5];
        String kejadian = (String) params[6];
        String tglkejadian = (String) params[7];
        String judul = (String) params[8];
//        String kebutuhan_dana = (String) params[9];
        String digunakanuntuk = (String) params[9];
        String periode = (String) params[10];
        String cerita = (String) params[11];
        Bitmap gambar = (Bitmap) params[12];
//        String buku = (String) params[14];
//        String sepatu = (String) params[15];
//        String elektronik = (String) params[16];
        // iteration array here from params 14
        ArrayList<String> donasiBarangs = new ArrayList<String>();
        for (int i = 13; i < params.length; i++) {
            Log.i("stuffshare", "params " + params[i].toString());
            donasiBarangs.add((String) params[i]);
        }

        ByteArrayOutputStream gambarBoas = new ByteArrayOutputStream();
        gambar.compress(Bitmap.CompressFormat.PNG, 1, gambarBoas);
        String data = "";

        try {
            HttpMultipartClient client = new HttpMultipartClient(url);
            client.connectForMultipart();
            client.addFormPart("userid", userid);
            client.addFormPart("kategori", kategori);
            client.addFormPart("penerima", penerima);
            client.addFormPart("nohp", nohp);
            client.addFormPart("alamat_penerima", alamat_penerima);
            client.addFormPart("kejadian", kejadian);
            client.addFormPart("tglkejadian", tglkejadian);
            client.addFormPart("judul", judul);
//            client.addFormPart("kebutuhan_dana", kebutuhan_dana);
            client.addFormPart("digunakanuntuk", digunakanuntuk);
            client.addFormPart("periode", periode);
            client.addFormPart("cerita", cerita);
            client.addFilePart("gambar", "png", gambarBoas.toByteArray());
//            client.addFormPart("donasibarang[]", buku);
//            client.addFormPart("donasibarang[]", sepatu);
//            client.addFormPart("donasibarang[]", elektronik);
            for (int i = 0; i < donasiBarangs.size(); i++) {
                client.addFormPart("donasibarang[]", donasiBarangs.get(i));
            }
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
