package com.stuff.stuffshare.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.activity.AlertHaveCampaignActivity;
import com.stuff.stuffshare.activity.AlertQuestionActivity;
import com.stuff.stuffshare.activity.SubmissionActivity;
import com.stuff.stuffshare.fragment.AccountPlusFragment;
import com.stuff.stuffshare.fragment.DonationFragment;
import com.stuff.stuffshare.fragment.InformationItemDonationFragment;
import com.stuff.stuffshare.fragment.MyDonationFragment;
import com.stuff.stuffshare.fragment.ScheduleDonationFragment;
import com.stuff.stuffshare.model.Item;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;

import static com.stuff.stuffshare.MainActivity.ShowFragment;

public class HomeAdapter extends ArrayAdapter {

    Context context;
    ArrayList <Item>homeItems = new ArrayList<>();
    StuffShareApp stuffShareApp;
    JSONObject jObj, resObj;
    SharedPrefManager sharedPrefManager;
    String masaDonasi;


    public HomeAdapter(Context context, int textViewResourceId, ArrayList <Item>value){
        super(context, textViewResourceId, value);
        this.homeItems = value;
        this.context = context;
        stuffShareApp = (StuffShareApp) this.context.getApplicationContext();
        sharedPrefManager = new SharedPrefManager(getContext());
    }

    @Override
    public int getCount() {
        return super.getCount();
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = convertView;
        if (v == null){
            LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = inflater.inflate(R.layout.grid_view_items, null);
//            TextView textView = (TextView) v.findViewById(R.id.textView);
            ImageView imageView = (ImageView) v.findViewById(R.id.iVHome);
            imageView.setImageResource(homeItems.get(position).getImage());
//            imageView.setForegroundGravity(Gravity.CENTER);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.getAdjustViewBounds();
//            imageView.getLayoutParams().width = 800;
            imageView.getLayoutParams().height = 400;

        }
        int type = position/1;
        switch (type){
            case 0:
//                v.setBackgroundResource(R.drawable.ikon_donasi);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DonationFragment donationFragment = new DonationFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, donationFragment, fragmentManager);
                    }
                });
                break;
            case 1:
//                v.setBackgroundResource(R.drawable.add_donation_rounded);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (stuffShareApp.getData().getIduser().equals(sharedPrefManager.getSPUserid()) && stuffShareApp.getData().getMasaDonasi() > 0){
                            Intent goAlertHaveCampaignActivity = new Intent(getContext(), AlertHaveCampaignActivity.class);
                            context.startActivity(goAlertHaveCampaignActivity);
                        } else {
                            Intent goAlertQuestionActivity = new Intent(getContext(), AlertQuestionActivity.class);
                            context.startActivity(goAlertQuestionActivity);
                        }
//                        getData();

//                        ;
                    }
                });
                break;
            case 2:
//                v.setBackgroundResource(R.drawable.account_plus_rounded);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        AccountPlusFragment accountPlusFragment = new AccountPlusFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, accountPlusFragment, fragmentManager);
                    }
                });
                break;
            case 3:
//                v.setBackgroundResource(R.drawable.schedule_donation_rounded);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        ScheduleDonationFragment scheduleDonationFragment = new ScheduleDonationFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, scheduleDonationFragment, fragmentManager);
                    }
                });
                break;
            case 4:
//                v.setBackgroundResource(R.drawable.information_donation_rounded);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        InformationItemDonationFragment informationItemDonationFragment = new InformationItemDonationFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, informationItemDonationFragment, fragmentManager);
                    }
                });
                break;
            case 5:
//                v.setBackgroundResource(R.drawable.status_donation_rounded);
                v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        MyDonationFragment myDonationFragment = new MyDonationFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, myDonationFragment, fragmentManager);
                    }
                });
                break;
        }
        return v;
    }

    public void dialog() {
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setView(R.layout.alert_dialog_layout);
//        alertDialog.setTitle("Untuk mengajukan penggalangan donasi anda harus terdaftar di akun plus");
        alertDialog.setPositiveButton("Sudah",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent goSubmissionActivity = new Intent(getContext(), SubmissionActivity.class);
                        context.startActivity(goSubmissionActivity);
                    }
                });
        alertDialog.setNegativeButton("Belum, Daftar sekarang",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        AccountPlusFragment accountPlusFragment = new AccountPlusFragment();
                        Activity activity = (Activity) context;
                        FragmentManager fragmentManager = ((FragmentActivity) getContext()).getSupportFragmentManager();
                        ShowFragment(R.id.fl_container, accountPlusFragment, fragmentManager);
                    }
                });
        alertDialog.show();

        return;
    }
}
