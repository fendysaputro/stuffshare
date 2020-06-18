package com.stuff.stuffshare.activity;

import android.app.Activity;
import android.os.Bundle;

import androidx.annotation.Nullable;

import com.stuff.stuffshare.R;

public class NotificationActivity extends Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_activity);
    }
}
