package com.stuff.stuffshare.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.squareup.picasso.Picasso;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.adapter.CollectDonationAdapter;
import com.stuff.stuffshare.adapter.ListDonationAdapter;
import com.stuff.stuffshare.model.Campaigner;
import com.stuff.stuffshare.model.CategoryBarang;
import com.stuff.stuffshare.model.Client;
import com.stuff.stuffshare.model.RowItem;
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
import java.util.HashMap;
import java.util.List;

import es.dmoral.toasty.Toasty;

public class CollectDonationActivity extends AppCompatActivity {

    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    ListView listView;
    ArrayList<CategoryBarang> campaigners;
    CollectDonationAdapter collectDonationAdapter = null;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collect_donation_activity);

        stuffShareApp = (StuffShareApp) getApplication();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.love);
            getSupportActionBar().setTitle("");
        }

        ImageView imageView = (ImageView) findViewById(R.id.image);
        if (stuffShareApp.getSelectedCampaigner().getImageCampaign().equals("")){
            imageView.setImageResource(R.drawable.logo);
        } else {
            Picasso.with(this)
                    .load(stuffShareApp.getSelectedCampaigner().getImageCampaign())
                    .fit()
                    .into(imageView);
        }

        TextView noteTitle = (TextView) findViewById(R.id.txtNote);
        noteTitle.setText("Berbagi Barang Bekas Kepada Orang yang Membutuhkan");

        TextView infoNote = (TextView) findViewById(R.id.txtInfoDonationNote);
        infoNote.setText("Informasi Penggalangan Donasi");

        TextView jmlDonation = (TextView) findViewById(R.id.txtJmlDonation);
        jmlDonation.setText(stuffShareApp.getSelectedCampaigner().getCountDonation() + " Donasi");

        TextView donaturName = (TextView) findViewById(R.id.txtDonationName);
        donaturName.setText(stuffShareApp.getSelectedCampaigner().getTitleCampaign());

        TextView donationDays = (TextView) findViewById(R.id.txtDonaturDays);
        donationDays.setText(stuffShareApp.getSelectedCampaigner().getMasaDonasi() + " Hari");

        ImageView communityImage = (ImageView) findViewById(R.id.iconBicycle);
        Picasso.with(this)
                .load(stuffShareApp.getSelectedCampaigner().getImageCom())
                .fit()
                .into(communityImage);
//        communityImage.setImageResource(R.drawable.sepeda);

        TextView communityName = (TextView) findViewById(R.id.txtDonationCommunity);
        communityName.setText(stuffShareApp.getSelectedCampaigner().getOrganization());

        TextView hastagName = (TextView) findViewById(R.id.hastag);
        hastagName.setText("#BekalJadiBerkah");

        Button donationNowBtn = (Button) findViewById(R.id.btnDonation);
        donationNowBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent goListCollection = new Intent(CollectDonationActivity.this, CollectDonationListActivity.class);
                startActivity(goListCollection);
            }
        });

        campaigners = new ArrayList<CategoryBarang>();
        listView = (ListView) findViewById(R.id.lVcollectDonation);
        collectDonationAdapter = new CollectDonationAdapter(getApplicationContext(), R.layout.list_view_collect_donation_activity, campaigners);
        listView.setAdapter(collectDonationAdapter);

//        getData("", campaigners, collectDonationAdapter);
        getDataDonasi(stuffShareApp.getSelectedCampaigner(), campaigners, collectDonationAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.toolbar_nav_menu, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case android.R.id.home:
                Toast.makeText(this, "click..!!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.menuShare:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "jangan lupa untuk saling berbagi install stuffshare di playstore");
                sendIntent.setType("text/plain");

                Intent shareIntent = Intent.createChooser(sendIntent, stuffShareApp.TAG);

                try {
                    startActivity(shareIntent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toasty.warning(getApplication(), "Your Apps have not been installed", Toasty.LENGTH_SHORT, true).show();
                }

                return true;
            case R.id.menuAbout:
                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuSettings:
                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
                return true;
            case R.id.menuLogout:
                Toast.makeText(getApplicationContext(),"Item 3 Selected", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

//    public void getData (String data, ArrayList<CategoryBarang> campaigners, CollectDonationAdapter collectDonationAdapter) {
//        AsyncHttpTask campaignTask = new AsyncHttpTask("");
//        Log.i(stuffShareApp.TAG, "selected Id " + stuffShareApp.getSelectedCampaigner().getId());
//        campaignTask.execute(stuffShareApp.HOST + stuffShareApp.CAMPAIGN + stuffShareApp.getSelectedCampaigner().getId(), "GET");
//        campaignTask.setHttpResponseListener(new OnHttpResponseListener() {
//            @Override
//            public void OnHttpResponse(String response) {
//                try {
//                    JSONObject resObj = new JSONObject(response);
//                    if (resObj.getBoolean("r")){
//                        JSONArray resArray = resObj.getJSONArray("d");
//                        for (int i = 0; i < resArray.length(); i++) {
//                            JSONObject jObj = resArray.getJSONObject(i);
//                            Log.i(stuffShareApp.TAG, "response collect " + jObj);
//                            Campaigner campaigner = new Campaigner();
//                            campaigner.setId(jObj.getString("id"));
//                            campaigner.setImageCampaign(jObj.getString("gambar"));
//                            campaigner.setDesc(jObj.getString("kejadian"));
//                            campaigner.setTglBuat(jObj.getString("tgl_buat"));
//                            campaigner.setTglSelesai(jObj.getString("tglselesai"));
//                            campaigner.setOrganization(jObj.getString("organisasi"));
//                            campaigner.setCountDonation(jObj.getString("banyakdonasi"));
//                            String dateBeforeString = campaigner.getTglBuat();
//                            String dateAfterString = campaigner.getTglSelesai();
//                            LocalDate dateBefore = LocalDate.parse(dateBeforeString);
//                            LocalDate dateAfter = LocalDate.parse(dateAfterString);
//                            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
//                            String dateString = DateFormat.format("yyyy-MM-dd", new Date(noOfDaysBetween)).toString();
//                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
//                            LocalDate dayNow = LocalDate.parse(timeStamp);
//                            LocalDate dateMass = LocalDate.parse(dateString);
//                            long massDonation = ChronoUnit.DAYS.between(dayNow, dateAfter);
//                            campaigner.setMasaDonasi(String.valueOf(massDonation));
//                            campaigner.setDonasiBarang(jObj.getJSONArray("donasibarang"));
//                            for (int j = 0; j < campaigner.getDonasiBarang().length(); j++) {
//                                CategoryBarang categoryBarang = new CategoryBarang();
//                                categoryBarang.setId(campaigner.getDonasiBarang().getJSONObject(j).getString("id"));
//                                categoryBarang.setProductName(campaigner.getDonasiBarang().getJSONObject(j).getString("name"));
//                                categoryBarang.setCount(campaigner.getDonasiBarang().getJSONObject(j).getString("qty"));
//                                categoryBarang.setImageId(campaigner.getDonasiBarang().getJSONObject(j).getString("url"));
//                                campaigners.add(categoryBarang);
//                            }
//                            stuffShareApp.setCampaigner(campaigner);
//
//                        }
//                    }
//                    collectDonationAdapter.notifyDataSetChanged();
//                } catch (JSONException e){
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public void getDataDonasi (Campaigner campaigner, final ArrayList<CategoryBarang> categoryBarangs, CollectDonationAdapter collectDonationAdapter){
        if (campaigner.getDonasiBarang() != null){
            try {
                for (int i = 0; i < campaigner.getDonasiBarang().length(); i++) {
                    CategoryBarang categoryBarang = new CategoryBarang();
                    categoryBarang.setId(campaigner.getDonasiBarang().getJSONObject(i).getString("id"));
                    categoryBarang.setProductName(campaigner.getDonasiBarang().getJSONObject(i).getString("name"));
                    categoryBarang.setCount(campaigner.getDonasiBarang().getJSONObject(i).getString("qty"));
                    categoryBarang.setImageId(campaigner.getDonasiBarang().getJSONObject(i).getString("url"));
                    categoryBarangs.add(categoryBarang);
                }
                collectDonationAdapter.notifyDataSetChanged();
            } catch (JSONException e){
                e.getMessage();
            }
        }
    }
}
