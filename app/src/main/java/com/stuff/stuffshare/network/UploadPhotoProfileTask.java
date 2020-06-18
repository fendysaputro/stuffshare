package com.stuff.stuffshare.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Log;

import java.io.ByteArrayOutputStream;

public class UploadPhotoProfileTask extends AsyncTask<Object, Void, String> {

    OnHttpResponseListener onHttpResponseListener;

    public void setOnHttpResponseListener(OnHttpResponseListener listener) {
        onHttpResponseListener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        String userid = (String) params[1];
        Bitmap img_photo = (Bitmap) params[2];
        ByteArrayOutputStream photoProfileBaos = new ByteArrayOutputStream();
        img_photo.compress(Bitmap.CompressFormat.PNG, 1, photoProfileBaos);
        String data = "";

        try {
            HttpMultipartClient client = new HttpMultipartClient(url);
            client.connectForMultipart();
            client.addFormPart("id", userid);
            client.addFilePart("image", userid+".jpg", photoProfileBaos.toByteArray());
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
