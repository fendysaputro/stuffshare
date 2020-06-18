package com.stuff.stuffshare.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.Bank;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnGetDataFinish;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.AppUtils;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ThankyouActivity extends AppCompatActivity {

    TextView thankyouTitle, sendDonation, addressSent, transferDonation, noRekDonation, quote;
    Button backButton;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    AppUtils appUtils;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou_activity);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);
        appUtils = new AppUtils();

        thankyouTitle = (TextView) findViewById(R.id.txtThankyouTitle);
        sendDonation = (TextView) findViewById(R.id.txtSendDonation);
        addressSent = (TextView) findViewById(R.id.txtAddressSent);
        transferDonation = (TextView) findViewById(R.id.txtTransferDonation);
        noRekDonation = (TextView) findViewById(R.id.txtNorekDonation);
        quote = (TextView) findViewById(R.id.txtQuote);

        thankyouTitle.setText("Terima Kasih Telah Berdonasi");
        sendDonation.setText("Kirimkan Barang Donasi ke alamat berikut");
        addressSent.setText(stuffShareApp.getCampaigner().getAlamatPenyelenggara());
        String jmlUang = appUtils.formatRupiah(Double.parseDouble(stuffShareApp.getNominalDonation()));
        transferDonation.setText("Transfer Sejumlah " + jmlUang + " ke nomor rekening berikut");
        noRekDonation.setText(stuffShareApp.getBank().getNameBank() + " " + stuffShareApp.getBank().getNorek());
        quote.setText("satu kebaikan akan mengundang kebaikan lainnya");


        backButton = (Button) findViewById(R.id.btnBack);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goMainActivity = new Intent(ThankyouActivity.this, MainActivity.class);
                startActivity(goMainActivity);
            }
        });
    }
}
