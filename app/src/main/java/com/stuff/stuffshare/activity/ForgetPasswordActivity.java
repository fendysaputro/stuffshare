package com.stuff.stuffshare.activity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.util.SharedPrefManager;

import es.dmoral.toasty.Toasty;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button btnReset;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(getApplication());

        setContentView(R.layout.forget_password_activity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText("Lupa Password");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        editTextEmail = (EditText) findViewById(R.id.edEmail);
        editTextEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        btnReset = (Button) findViewById(R.id.resetPassword);
        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onResetPasswordButton();
            }
        });
    }

    public void onResetPasswordButton () {
        if (TextUtils.isEmpty(editTextEmail.getText())){
            Toasty.warning(getApplication(), "field tidak boleh kosong", Toasty.LENGTH_SHORT, true).show();
        } else {

        }
    }
}
