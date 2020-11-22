package com.stuff.stuffshare.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.adapter.InboxMessageAdapter;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.model.Message;

import java.util.ArrayList;
import java.util.List;

public class InboxMessageActivity extends AppCompatActivity {
    TextView toolbar_title;
    ArrayList<Message> messageArrayList;
    InboxMessageAdapter inboxMessageAdapter = null;
    ListView listView = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message);
        initView();
        setupToolbar();
        messageArrayList= new ArrayList<Message>();

        Message message, message1;

        message = new Message();
        message.setMessage("Permintaan akun plus anda di tolak");
        message.setDate("12 Oktober 2020");
        messageArrayList.add(message);

        message1 = new Message();
        message1.setMessage("Donasi anda telah di konfirmasi");
        message1.setDate("11 November 2020");
        messageArrayList.add(message1);

        inboxMessageAdapter = new InboxMessageAdapter(this, R.layout.item_list_inbox_message, messageArrayList);
        listView.setAdapter(inboxMessageAdapter);

    }

    private void initView() {
        toolbar_title = findViewById(R.id.toolbar_title);
        listView = findViewById(R.id.lv_inbox_message);
    }

    private void setupToolbar() {
        toolbar_title.setText(R.string.txt_inbox);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);
    }
}