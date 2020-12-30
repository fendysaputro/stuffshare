package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ForgetPasswordActivity extends AppCompatActivity {

    EditText editTextEmail;
    Button btnReset;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    String email;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(getApplication());

        setContentView(R.layout.forget_password_activity);

        TextView toolbar_title = findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.txt_lupa_password);
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
                email = editable.toString();
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
            editTextEmail.setError(getString(R.string.txt_email_required));
        } else {
            AsyncHttpTask mForgetPasswordTask = new AsyncHttpTask("email="+email);
            mForgetPasswordTask.execute(StuffShareApp.HOST + StuffShareApp.FORGET_PASSWORD, "POST");
            mForgetPasswordTask.setHttpResponseListener(new OnHttpResponseListener() {
                @Override
                public void OnHttpResponse(String response) {
                    Log.i(stuffShareApp.TAG, "response " + response);
                    try {
                        JSONObject resObj = new JSONObject(response);
                        if (resObj.getBoolean("r")){
                            Toasty.success(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                            Intent goLoginActivity = new Intent(ForgetPasswordActivity.this, LoginActivity.class);
                            startActivity(goLoginActivity);
                        }
                    } catch (JSONException e){
                        e.printStackTrace();
                        Toasty.info(getApplication(), "Email tidak ditemukan", Toasty.LENGTH_LONG).show();
                    }
                }
            });
        }
    }
}
