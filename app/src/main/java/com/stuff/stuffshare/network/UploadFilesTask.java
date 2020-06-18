package com.stuff.stuffshare.network;

import android.graphics.Bitmap;
import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;

public class UploadFilesTask extends AsyncTask<Object, Void, String> {

    OnHttpResponseListener onHttpResponseListener;

    public void setOnHttpResponseListener(OnHttpResponseListener listener) {
        onHttpResponseListener = listener;
    }

    @Override
    protected String doInBackground(Object... params) {
        String url = (String) params[0];
        String userid = (String) params[1];
        String organization = (String) params[2];
        String address = (String) params[3];
        String akta = (String) params[4];
        String penganggung_jawab = (String) params[5];
        String npwp = (String) params[6];
        Bitmap img_akta = (Bitmap) params[7];
        Bitmap img_npwp = (Bitmap) params[8];
        Bitmap img_organization = (Bitmap) params[9];
        ByteArrayOutputStream aktaBoas = new ByteArrayOutputStream();
        ByteArrayOutputStream npwpBoas = new ByteArrayOutputStream();
        ByteArrayOutputStream organizationBoas = new ByteArrayOutputStream();
        img_akta.compress(Bitmap.CompressFormat.PNG, 1, aktaBoas);
        img_npwp.compress(Bitmap.CompressFormat.PNG, 1, npwpBoas);
        img_organization.compress(Bitmap.CompressFormat.PNG, 1, organizationBoas);
        String data = "";

        try {
            HttpMultipartClient client = new HttpMultipartClient(url);
            client.connectForMultipart();
            client.addFormPart("userid", userid);
            client.addFormPart("organization", organization);
            client.addFormPart("address", address);
            client.addFormPart("akta", akta);
            client.addFormPart("penganggungjawab", penganggung_jawab);
            client.addFormPart("npwp", npwp);
            client.addFilePart("img_akta", "png", aktaBoas.toByteArray());
            client.addFilePart("img_npwp", "png", npwpBoas.toByteArray());
            client.addFilePart("img_organization", "png", organizationBoas.toByteArray());
            client.finishMultipart();
            data = client.getResponse();
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
