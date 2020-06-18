package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;

public class ThankyouCampaignerActivity extends AppCompatActivity {

    TextView thankyou, verification;
    Button goBack;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.thankyou_campaigner);

        thankyou = (TextView) findViewById(R.id.txtThankyouTitle);
        verification = (TextView) findViewById(R.id.txtVerification);

        thankyou.setText("Terima kasih telah melakukan pengajuan penggalangan donasi.");
        verification.setText("Pengajuan penggalangan donasi akan diverifikasi terlebih dahulu oleh admin");

        goBack = (Button) findViewById(R.id.btnBackThankyouCampaigner);
        goBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goBackMain = new Intent(getApplication(), MainActivity.class);
                startActivity(goBackMain);
            }
        });
    }
}
