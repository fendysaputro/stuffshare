package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.ContentView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.model.User;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpCancel;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {

    TextView txtTitle;
    EditText edEmail, edPassword;
    Button btnLogin, btnRegister;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    TextView forgetPassword;
    ProgressBar mProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        setContentView(R.layout.login_activity);

        txtTitle = (TextView) findViewById(R.id.title);

        edEmail = (EditText) findViewById(R.id.emailTxt);
        edPassword = (EditText) findViewById(R.id.passwordTxt);

        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);

        btnLogin = (Button) findViewById(R.id.loginBtn);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBtnLogin();
            }
        });

        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goRegister = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(goRegister);
            }
        });

        forgetPassword = (TextView) findViewById(R.id.tVForgetPassword);
        forgetPassword.setText(R.string.txt_forget_password);
        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent toForgetPassword = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                startActivity(toForgetPassword);
            }
        });

        if (sharedPrefManager.getSPSudahLogin()){
            startActivity(new Intent(LoginActivity.this, MainActivity.class)
                    .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
            finish();
        }
    }

    private void onBtnLogin() {
        mProgressBar.setVisibility(View.VISIBLE);
        if (TextUtils.isEmpty(edPassword.getText())){
            edPassword.setError(getString(R.string.txt_password_must));
        }

        if (TextUtils.isEmpty(edEmail.getText())){
            edEmail.setError(getString(R.string.txt_email_required));
        } else {
            if (edEmail.getText().toString().trim().matches(emailPattern)){
                AsyncHttpTask mLoginTask = new AsyncHttpTask("email="+edEmail.getText()+"&password="+edPassword.getText());
                mLoginTask.execute(StuffShareApp.HOST + StuffShareApp.LOGIN_PATH, "POST");
                mLoginTask.setHttpResponseListener(new OnHttpResponseListener() {
                    @Override
                    public void OnHttpResponse(String response) {
                        Log.i(stuffShareApp.TAG, "response " + response);
                        try {
                            JSONObject resObj = new JSONObject(response);
                            if (resObj.getBoolean("r")){
                                JSONObject dataObj = resObj.getJSONObject("d");
                                stuffShareApp.setLogin(true);
                                User user = new User(dataObj.getString("id"),
                                        dataObj.getString("name"),
                                        dataObj.getString("phone"),
                                        dataObj.getString("email"),
                                        dataObj.getInt("akunplus"),
                                        dataObj.getString("token"),
                                        dataObj.getString("image"),
                                        dataObj.getString("status_akunplus"));
                                stuffShareApp.setUser(user);
                                btnLogin.setEnabled(true);

                                String nama = dataObj.getString("name");
                                String phone = dataObj.getString("phone");
                                String email = dataObj.getString("email");
                                String userid = dataObj.getString("id");
                                int akunplus = dataObj.getInt("akunplus");
                                String image = dataObj.getString("image");
                                String status_ap = dataObj.getString("status_akunplus");
                                sharedPrefManager.saveSPString(SharedPrefManager.name, nama);
                                sharedPrefManager.saveSPString(SharedPrefManager.phone, phone);
                                sharedPrefManager.saveSPString(SharedPrefManager.email, email);
                                sharedPrefManager.saveSPString(SharedPrefManager.userid, userid);
                                sharedPrefManager.saveSPInt("akunplus", akunplus);
                                sharedPrefManager.saveSPString(SharedPrefManager.image, image);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.login, true);
                                sharedPrefManager.saveSPString(SharedPrefManager.statusAccountPlus, status_ap);
                                if (dataObj.getInt("akunplus") == 1){
                                    String imageCom = dataObj.getString("foto_penyelenggara");
                                    Log.i(stuffShareApp.TAG, "img_penyelenggara " + imageCom);
                                    sharedPrefManager.saveSPString(SharedPrefManager.image_community, imageCom);
                                }
                                startActivity(new Intent(getApplication(), MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                Toasty.success(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                                finish();
                            } else {
                                mProgressBar.setVisibility(View.GONE);
                                Toasty.info(getApplication(), "Invalid User ID or Password", Toasty.LENGTH_LONG).show();
                                btnLogin.setEnabled(true);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toasty.info(getApplication(), "Invalid User ID or Password", Toasty.LENGTH_LONG).show();
                            mProgressBar.setVisibility(View.GONE);
                        }
                    }
                });
                mLoginTask.setOnHttpCancel(new OnHttpCancel() {
                    @Override
                    public void OnHttpCancel() {
                    }
                });
            } else {
                edEmail.setError(getString(R.string.txt_email_invalid));
                mProgressBar.setVisibility(View.GONE);
            }
        }
    }
}
