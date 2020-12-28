package com.stuff.stuffshare.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.InboxMessageAdapter;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.model.Message;
import com.stuff.stuffshare.model.MessageUser;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InboxMessageActivity extends AppCompatActivity {
    TextView toolbar_title;
    ArrayList<MessageUser> messageArrayList;
    InboxMessageAdapter inboxMessageAdapter = null;
    ListView listView = null;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inbox_message);
        stuffShareApp = (StuffShareApp) this.getApplicationContext();
        sharedPrefManager = new SharedPrefManager(getApplicationContext());
        initView();
        setupToolbar();
        initialize();
        messageArrayList= new ArrayList<MessageUser>();

        inboxMessageAdapter = new InboxMessageAdapter(this, R.layout.item_list_inbox_message, messageArrayList);
        listView.setAdapter(inboxMessageAdapter);

        getData();
    }

    private void initView() {
        toolbar_title = findViewById(R.id.toolbar_title);
        listView = findViewById(R.id.lv_inbox_message);
    }

    private void initialize() {
        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(getApplication());
    }

    private void setupToolbar() {
        toolbar_title.setText(R.string.txt_inbox);
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);
    }

    public void getData(){
        getDataMessage("", messageArrayList, inboxMessageAdapter);
    }

    public void getDataMessage(String data, final ArrayList<MessageUser> messages, InboxMessageAdapter inboxMessageAdapter) {
        AsyncHttpTask messageTask = new AsyncHttpTask("");
        messageTask.execute(StuffShareApp.HOST + StuffShareApp.MESSAGE_USER + sharedPrefManager.getSPUserid(), "GET");
        messageTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jsonObject = resArray.getJSONObject(i);
                            Message message = new Message();
                            message.setUserId(jsonObject.getString("id"));
                            message.setName(jsonObject.getString("name"));
                            message.setAlamat(jsonObject.getString("alamat"));
                            message.setPhone(jsonObject.getString("phone"));
                            message.setStatus(jsonObject.getString("status"));
                            message.setTotal_message(jsonObject.getInt("total_message"));
                            message.setTotal_unread(jsonObject.getInt("total_unread"));
                            message.setTotal_read(jsonObject.getInt("total_read"));
                            message.setMessageUser(jsonObject.getJSONArray("message"));
                            if (message.getMessageUser() != null) {
                                JSONArray mesArray = message.getMessageUser();
                                for (int j = 0; j < mesArray.length(); j++) {
                                    JSONObject mesObj = mesArray.getJSONObject(j);
                                    MessageUser messageUser = new MessageUser();
                                    messageUser.setNo(mesObj.getInt("no"));
                                    messageUser.setId(mesObj.getInt("id"));
                                    messageUser.setCategory(mesObj.getString("category"));
                                    messageUser.setDate(mesObj.getString("date"));
                                    messageUser.setText(mesObj.getString("text"));
                                    messageUser.setStatus(mesObj.getString("status"));
                                    messages.add(messageUser);
                                }
                            }
                        }
                        inboxMessageAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }
}