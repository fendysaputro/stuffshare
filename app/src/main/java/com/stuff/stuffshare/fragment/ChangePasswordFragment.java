package com.stuff.stuffshare.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONException;
import org.json.JSONObject;

import es.dmoral.toasty.Toasty;

public class ChangePasswordFragment extends Fragment {

    EditText oldPass, newPass, confirmPass;
    Button saveBtn;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_change_password, container, false);

        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        sharedPrefManager = new SharedPrefManager(getContext());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText(R.string.txt_password_title);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);

        oldPass = (EditText) view.findViewById(R.id.edOldPassword);
        newPass = (EditText) view.findViewById(R.id.edNewPassword);
        confirmPass = (EditText) view.findViewById(R.id.edConfirmNewPassword);

        saveBtn = (Button) view.findViewById(R.id.saveChangePassword);
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChangePassword();
            }
        });

        return view;
    }

    private void onChangePassword () {
        AsyncHttpTask mChangePassword = new AsyncHttpTask("passwordsaatini="+oldPass.getText() +
                "&passwordbaru="+newPass.getText() +
                "&ulangipasswordbaru="+confirmPass.getText() +
                "&userid="+sharedPrefManager.getSPUserid());
        mChangePassword.execute(StuffShareApp.HOST + StuffShareApp.CHANGE_PASSWORD, "POST");
        mChangePassword.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        Toasty.success(getActivity(), "Change Password Success " + resObj.getString("m")).show();
                        Intent goMain = new Intent(getActivity(), MainActivity.class);
                        startActivity(goMain);
                    } else {
                        Toasty.warning(getActivity(), resObj.getString("m"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}
