package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpCancel;
import com.stuff.stuffshare.network.OnHttpResponseListener;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class RegisterActivity extends AppCompatActivity {

    EditText nameTxt, phoneTxt, emailTxt, passwordTxt;
    Button btnRegister;
    StuffShareApp stuffShareApp;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        stuffShareApp = (StuffShareApp) getApplication();

        nameTxt = (EditText) findViewById(R.id.edName);
        phoneTxt = (EditText) findViewById(R.id.edPhone);
        emailTxt = (EditText) findViewById(R.id.edEmail);
        passwordTxt = (EditText) findViewById(R.id.edPassword);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnRegister();
            }
        });
    }

    public void onBtnRegister () {
        mProgressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(nameTxt.getText()) && TextUtils.isEmpty(phoneTxt.getText()) &&
                TextUtils.isEmpty(emailTxt.getText()) && TextUtils.isEmpty(passwordTxt.getText())){
            Toasty.warning(getApplication(), "field tidak boleh kosong", Toasty.LENGTH_SHORT, true).show();
        } else {
            if (emailTxt.getText().toString().trim().matches(emailPattern)){
                AsyncHttpTask mRegisterTask = new AsyncHttpTask("name="+nameTxt.getText()
                        +"&phone="+phoneTxt.getText()
                        +"&email="+emailTxt.getText()
                        +"&password="+passwordTxt.getText());
                Log.i(stuffShareApp.TAG, "Link " + stuffShareApp.HOST + stuffShareApp.REGISTER_PATH);
                mRegisterTask.execute(stuffShareApp.HOST + stuffShareApp.REGISTER_PATH, "POST");
                mRegisterTask.setHttpResponseListener(new OnHttpResponseListener() {
                    @Override
                    public void OnHttpResponse(String response) {
                        Log.i(stuffShareApp.TAG, "response " + response);
                        try {
                            JSONObject resObj = new JSONObject(response);
                            if (resObj.getBoolean("r")){
                                Toasty.success(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                                Intent goLoginActivity = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(goLoginActivity);
                                finish();
                            } else {
                                Toasty.warning(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                                mProgressBar.setVisibility(View.GONE);
                            }
                        } catch (JSONException e){
                            e.printStackTrace();
                        }
                    }
                });
                mRegisterTask.setOnHttpCancel(new OnHttpCancel() {
                    @Override
                    public void OnHttpCancel() {

                    }
                });
            } else {
                Toasty.warning(getApplication(), "email invalid", Toasty.LENGTH_SHORT, true).show();
            }
        }
    }
}
