package com.stuff.stuffshare.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.adapter.InboxMessageAdapter;
import com.stuff.stuffshare.model.Message;

import java.util.ArrayList;

public class InboxMessageActivity extends AppCompatActivity {
    TextView toolbar_title;
    public static final String[] messageDescription = new String[] {
            "Donasi anda berhasil",
            "Akun plus anda diterima"
    };

    public static final String[] messageDate = new String[] {
            "10 Des 2020, 10.00",
            "12 Nov 2020, 11.00"
    };

    ArrayList<Message> messageArrayList;
    InboxMessageAdapter inboxMessageAdapter = null;
    RecyclerView recyclerView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message);
        initView();
        setupToolbar();
        inboxMessageAdapter = new InboxMessageAdapter(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(inboxMessageAdapter);
    }

    private void initView() {
        toolbar_title = findViewById(R.id.toolbar_title);
        recyclerView = findViewById(R.id.rv_inbox_message);
    }

    private void setupToolbar() {
        toolbar_title.setText(R.string.txt_inbox);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);
    }
}