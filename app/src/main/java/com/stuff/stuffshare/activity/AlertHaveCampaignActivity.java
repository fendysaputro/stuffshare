package com.stuff.stuffshare.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.util.SharedPrefManager;

public class AlertHaveCampaignActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    Context context;
    SharedPrefManager sharedPrefManager;
    TextView infoCampaign;
    Button backCampaign;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.have_campaign_layout);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        infoCampaign = (TextView) findViewById(R.id.txtInfoCampaign);
        infoCampaign.setText(R.string.txt_info_campaign);

        backCampaign = (Button) findViewById(R.id.btnBackCampaigner);
        backCampaign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertHaveCampaignActivity.super.onBackPressed();
            }
        });

    }
}
