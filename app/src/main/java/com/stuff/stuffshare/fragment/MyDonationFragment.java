package com.stuff.stuffshare.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.MyDonationAdapter;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.Donation;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.ViewGroup.FOCUS_BLOCK_DESCENDANTS;
import static com.stuff.stuffshare.MainActivity.ShowFragment;

public class MyDonationFragment extends Fragment {

    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    public static final String[] communityName = new String[] {
            "#BekasJadiBerkah",
            "Bakti sosial peduli korban banjir",
            "Punya Barang Tak terpakai?"
    };


    public static final Integer[] images = {
            R.drawable.berkah,
            R.drawable.tanggap,
            R.drawable.wakaf
    };
    ArrayList<Donation> donations;
    MyDonationAdapter myDonationAdapter = null;
    ListView androidListView = null;
    Context context;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_donation, container, false);

        stuffShareApp = (StuffShareApp) getActivity().getApplication();
        sharedPrefManager = new SharedPrefManager(getActivity());

        TextView toolbar_title = view.findViewById(R.id.toolbar_title);
        toolbar_title.setText("Donasi Saya");
        toolbar_title.setTextColor(getResources().getColor(R.color.textColorToolbar));
        toolbar_title.setTextSize(30);
        
        donations = new ArrayList<Donation>();
        androidListView = (ListView) view.findViewById(R.id.itemListViewHistory);
        myDonationAdapter = new MyDonationAdapter(getActivity(), R.layout.list_view_my_donation, donations);
        androidListView.setAdapter(myDonationAdapter);

        getDataDonation("", donations, myDonationAdapter);


        androidListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                Donation donation = (Donation) donations.get(position);
//                stuffShareApp.setSelectedDonation(donation);
//                ConfirmationFragment confirmationFragment = new ConfirmationFragment();
//                Activity activity = (Activity) context;
//                FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
//                ShowFragment(R.id.fl_container, confirmationFragment, fragmentManager);
                Log.i(stuffShareApp.TAG, "touch");
            }
        });

        return view;
    }

    public void getDataDonation (String data, ArrayList<Donation> donations, MyDonationAdapter myDonationAdapter) {
        AsyncHttpTask mDonationTask = new AsyncHttpTask("");
        mDonationTask.execute(stuffShareApp.HOST + stuffShareApp.DONATION + sharedPrefManager.getSPUserid(), "GET");
        mDonationTask.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    JSONObject resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            JSONObject jObj = resArray.getJSONObject(i);
                            Donation donation = new Donation();
                            donation.setId(jObj.getString("id"));
                            donation.setUserId(jObj.getString("userid"));
                            donation.setPenggalangan(jObj.getString("penggalangan"));
                            donation.setAlamatPenyelenggara(jObj.getString("alamat_penyelenggara"));
                            donation.setGambar(jObj.getString("gambar"));
                            donation.setBankName(jObj.getString("bank"));
                            donation.setDate(jObj.getString("date"));
                            donation.setDonasiUang(jObj.getString("donasi"));
                            donation.setDonasiBarang(jObj.getJSONArray("donasibarang"));
                            donation.setMetodeBayar(jObj.getString("metodebayar"));
                            donation.setMetodeKirim(jObj.getString("metodekirim"));
                            donation.setStatus(jObj.getString("status"));
                            donations.add(donation);
                        }
                    }
                    myDonationAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
