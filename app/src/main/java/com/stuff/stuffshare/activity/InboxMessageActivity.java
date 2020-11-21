package com.stuff.stuffshare.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.stuff.stuffshare.R;

public class InboxMessageActivity extends AppCompatActivity {
    TextView toolbar_title;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message);
        initView();
        setupToolbar();
    }

    private void initView() {
        toolbar_title = findViewById(R.id.toolbar_title);
    }

    private void setupToolbar() {
        toolbar_title.setText(R.string.txt_inbox);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);
    }
}