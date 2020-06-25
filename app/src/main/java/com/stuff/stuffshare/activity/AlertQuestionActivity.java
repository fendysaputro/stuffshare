package com.stuff.stuffshare.activity;

import android.app.Activity;
import android.content.ContentProvider;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.fragment.AccountPlusFragment;
import com.stuff.stuffshare.util.SharedPrefManager;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.MainActivity.ShowFragment;
import static java.security.AccessController.getContext;

public class AlertQuestionActivity extends AppCompatActivity {

    Button btnSdh, btnBlm;
    StuffShareApp stuffShareApp;
    Context context;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alert_dialog_layout);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        btnSdh = (Button) findViewById(R.id.btnSudah);
        btnBlm = (Button) findViewById(R.id.btnBelum);

        btnSdh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int checkAkunPlus = sharedPrefManager.getSPAkunplus();
                String statusAkunPlus = sharedPrefManager.getSPStatusAkunPlus();
                if (checkAkunPlus == 1){
                    if (statusAkunPlus.equals("1")){
                        Intent goSubmissionActivity = new Intent(getApplication(), SubmissionActivity.class);
                        startActivity(goSubmissionActivity);
                    } else {
                        Toasty.warning(getApplication(), "akun anda belum di verifikasi oleh admin mohon tunggu sampai terverifikasi", Toasty.LENGTH_SHORT, true).show();
                    }
                } else {
                    Toasty.warning(getApplication(), "akun anda belum akun plus silahkan daftar akun plus dahulu", Toasty.LENGTH_SHORT, true).show();
                }
            }
        });

        btnBlm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toasty.warning(getApplication(), "Silahkan daftar pada menu Akun Plus", Toasty.LENGTH_SHORT, true).show();
                Intent goMainActivity = new Intent(getApplication(), MainActivity.class);
                startActivity(goMainActivity);
            }
        });
    }
}
