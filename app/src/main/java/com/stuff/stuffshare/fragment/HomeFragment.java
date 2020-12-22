package com.stuff.stuffshare.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.CollectDonationListActivity;
import com.stuff.stuffshare.activity.InboxMessageActivity;
import com.stuff.stuffshare.activity.ThankyouActivity;
import com.stuff.stuffshare.adapter.HomeAdapter;
import com.stuff.stuffshare.model.Item;
import com.stuff.stuffshare.model.Message;
import com.stuff.stuffshare.model.MessageUser;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class HomeFragment extends Fragment {
    GridView itemList;
    ArrayList itemHomeList = new ArrayList<>();
    HomeAdapter adapter = null;
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    ArrayList messageHome = new ArrayList<Message>();
    ImageView ivInbox;
    ImageView ivMark;
    boolean isNotEmptyData = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        ivInbox = view.findViewById(R.id.iv_message);
        ivMark = view.findViewById(R.id.notsopened);

        initialize();
        getData();

        itemHomeList.clear();

        itemList = (GridView) view.findViewById(R.id.itemHomeGrid);
        itemHomeList.add(new Item(R.drawable.ikon_donasi));
        itemHomeList.add(new Item(R.drawable.pengajuan_donasi));
        itemHomeList.add(new Item(R.drawable.akun_plus));
        itemHomeList.add(new Item(R.drawable.jadwal_donasi));
        itemHomeList.add(new Item(R.drawable.daftar_donasi));
        itemHomeList.add(new Item(R.drawable.status_donasi));

        adapter = new HomeAdapter(getContext(), R.layout.grid_view_items, itemHomeList);
        itemList.setAdapter(adapter);

        ivInbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInboxMessage();
            }
        });

        return view;
    }

    private void initialize() {
        stuffShareApp = (StuffShareApp) getActivity().getApplicationContext();
        sharedPrefManager = new SharedPrefManager(getActivity());
        messageHome = new ArrayList<Message>();
    }

    private void getData() {
        getDataMessage();
    }

    private void getDataMessage() {
        AsyncHttpTask messageTask = new AsyncHttpTask("");
        messageTask.execute(stuffShareApp.HOST + stuffShareApp.MESSAGE_USER + sharedPrefManager.getSPUserid(), "GET");
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
                                    stuffShareApp.setMessageUser(mesArray);
                                    if (stuffShareApp.getMessageUser().length() != 0) {
                                        ivMark.setVisibility(View.VISIBLE);
                                        isNotEmptyData = true;
                                    }
                                }
                            }
                           messageHome.add(message);
                        }
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        });
    }

    private void getInboxMessage(){
        if (isNotEmptyData) {
            Intent goInboxActivity = new Intent(getContext(), InboxMessageActivity.class);
            startActivity(goInboxActivity);
        } else {
            Toasty.info(getActivity(), "Anda tidak memiliki pesan", Toasty.LENGTH_SHORT).show();
        }
    }
}
