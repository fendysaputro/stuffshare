package com.stuff.stuffshare.activity;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.R;

public class ForgetPasswordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.forget_password_activity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Lupa Password");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

    }
}
