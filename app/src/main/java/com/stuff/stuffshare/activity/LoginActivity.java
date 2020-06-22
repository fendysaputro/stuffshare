package com.stuff.stuffshare.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(this);

        setContentView(R.layout.login_activity);

        txtTitle = (TextView) findViewById(R.id.title);

        edEmail = (EditText) findViewById(R.id.emailTxt);
        edPassword = (EditText) findViewById(R.id.passwordTxt);

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
        forgetPassword.setText("Lupa Password?");
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
        if (TextUtils.isEmpty(edEmail.getText()) && !TextUtils.isEmpty(edPassword.getText())){
            Toasty.warning(getApplication(), "both field can't be empty ", Toasty.LENGTH_SHORT, true).show();
        } else {
            if (edEmail.getText().toString().trim().matches(emailPattern)){
                AsyncHttpTask mLoginTask = new AsyncHttpTask("email="+edEmail.getText()+"&password="+edPassword.getText());
                mLoginTask.execute(stuffShareApp.HOST + stuffShareApp.LOGIN_PATH, "POST");
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
                                        dataObj.getString("image"));
                                stuffShareApp.setUser(user);
                                btnLogin.setEnabled(true);

                                String nama = dataObj.getString("name");
                                String phone = dataObj.getString("phone");
                                String email = dataObj.getString("email");
                                String userid = dataObj.getString("id");
                                int akunplus = dataObj.getInt("akunplus");
                                String image = dataObj.getString("image");
                                sharedPrefManager.saveSPString(SharedPrefManager.name, nama);
                                sharedPrefManager.saveSPString(SharedPrefManager.phone, phone);
                                sharedPrefManager.saveSPString(SharedPrefManager.email, email);
                                sharedPrefManager.saveSPString(SharedPrefManager.userid, userid);
                                sharedPrefManager.saveSPInt("akunplus", akunplus);
                                sharedPrefManager.saveSPString(SharedPrefManager.image, image);
                                sharedPrefManager.saveSPBoolean(SharedPrefManager.login, true);
                                if (dataObj.getInt("akunplus") == 1){
                                    String imageCom = dataObj.getString("foto_penyelenggara");
                                    Log.i(stuffShareApp.TAG, "img_penyelenggara " + imageCom);
                                    sharedPrefManager.saveSPString(SharedPrefManager.image_community, imageCom);
//                            stuffShareApp.setImage_community(imageCom);
                                }
                                startActivity(new Intent(getApplication(), MainActivity.class)
                                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK));
                                Toasty.success(getApplication(), resObj.getString("m"), Toasty.LENGTH_SHORT, true).show();
                                finish();
                            } else {
                                Toasty.info(getApplication(), "Invalid User ID or Password", Toasty.LENGTH_LONG).show();
                                btnLogin.setEnabled(true);
                            }
                        }catch (JSONException e){
                            e.printStackTrace();
                            Toasty.info(getApplication(), "Invalid User ID or Password", Toasty.LENGTH_LONG).show();
                        }
                    }
                });
                mLoginTask.setOnHttpCancel(new OnHttpCancel() {
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
