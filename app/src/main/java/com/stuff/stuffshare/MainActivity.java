package com.stuff.stuffshare;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.threeten.bp.DayOfWeek;
import org.threeten.bp.LocalDate;
import org.threeten.bp.ZoneId;
import org.threeten.bp.temporal.ChronoUnit;
import org.threeten.bp.temporal.TemporalAdjusters;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.stuff.stuffshare.activity.AlertHaveCampaignActivity;
import com.stuff.stuffshare.activity.AlertQuestionActivity;
import com.stuff.stuffshare.activity.NotificationActivity;
import com.stuff.stuffshare.fragment.HomeFragment;
import com.stuff.stuffshare.fragment.MyDonationFragment;
import com.stuff.stuffshare.fragment.ProfileFragment;
import com.stuff.stuffshare.model.Data;
import com.stuff.stuffshare.network.AsyncHttpTask;
import com.stuff.stuffshare.network.OnHttpResponseListener;
import com.stuff.stuffshare.service.BootReceiver;
import com.stuff.stuffshare.service.NoficationService;
import com.stuff.stuffshare.service.NotificationHelper;
import com.stuff.stuffshare.util.SharedPrefManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
//import java.time.LocalDate;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    StuffShareApp stuffShareApp;
    SharedPrefManager sharedPrefManager;
    JSONObject jObj, resObj;
    String masaDonasi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestPermission();
        setContentView(R.layout.activity_main);

        stuffShareApp = (StuffShareApp) getApplication();
        sharedPrefManager = new SharedPrefManager(getApplication());

        getData();

        myAlarm();

        loadFragment(new HomeFragment());
        BottomNavigationView bottomNavigationView = findViewById(R.id.nav_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);

    }

    public void myAlarm() {

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 21);
        calendar.set(Calendar.MINUTE, 45);

        if (stuffShareApp.isNotification()){
            if (calendar.getTime().compareTo(new Date()) < 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1);

            Intent intent = new Intent(getApplicationContext(), BootReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

            if (alarmManager != null) {
                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
            }
        }
    }

    public void getData () {
        AsyncHttpTask getCampaign = new AsyncHttpTask("");
        Log.i(stuffShareApp.TAG, "url " + stuffShareApp.HOST + stuffShareApp.ALL_CAMPAIGN + sharedPrefManager.getSPUserid() + "/status" + "/1");
        getCampaign.execute(stuffShareApp.HOST + stuffShareApp.ALL_CAMPAIGN + sharedPrefManager.getSPUserid() + "/status" + "/1", "GET");
        getCampaign.setHttpResponseListener(new OnHttpResponseListener() {
            @Override
            public void OnHttpResponse(String response) {
                try {
                    resObj = new JSONObject(response);
                    if (resObj.getBoolean("r")){
                        JSONArray resArray = resObj.getJSONArray("d");
                        for (int i = 0; i < resArray.length(); i++) {
                            jObj = resArray.getJSONObject(i);
                            Data data = new Data();
                            data.setIduser(jObj.getString("iduser"));
                            data.setTglBuat(jObj.getString("tgl_buat"));
                            String dateBeforeString = jObj.getString("tgl_buat");
                            String dateAfterString = jObj.getString("tglselesai");
                            LocalDate dateBefore = LocalDate.parse(dateBeforeString);
                            LocalDate dateAfter = LocalDate.parse(dateAfterString);
                            long noOfDaysBetween = dateAfter.until(dateBefore, org.threeten.bp.temporal.ChronoUnit.DAYS);
                            String dateString = DateFormat.format("yyyy-MM-dd", new Date(noOfDaysBetween)).toString();
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            LocalDate dayNow = LocalDate.parse(timeStamp);
                            LocalDate dateMass = LocalDate.parse(dateString);
                            long massDonation = dayNow.until(dateAfter, org.threeten.bp.temporal.ChronoUnit.DAYS);
                            Log.i(stuffShareApp.TAG, "masa donasi main " + massDonation);
                            int ms = (int)massDonation;
                            data.setMasaDonasi(ms);
                            stuffShareApp.setData(data);
                            Log.i(stuffShareApp.TAG, "user 3 " + stuffShareApp.getData().getIduser());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public boolean loadFragment(Fragment fragment){
        if (fragment != null){
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem){
        Fragment fragment = null;
        switch (menuItem.getItemId()){
            case R.id.navigation_home:
                fragment = new HomeFragment();
                break;
            case R.id.navigation_list_donation:
                fragment = new MyDonationFragment();
                break;
            case R.id.navigation_add_donation:
                if (stuffShareApp.getData() == null){
                    Intent goAlertQuestionActivity = new Intent(getApplication(), AlertQuestionActivity.class);
                    startActivity(goAlertQuestionActivity);
                } else {
                    if (!stuffShareApp.getData().getIduser().isEmpty()){
                        Log.i(stuffShareApp.TAG, "user 1 " + stuffShareApp.getData().getIduser());
                        Log.i(stuffShareApp.TAG, "user 2 " + sharedPrefManager.getSPUserid());
                        Log.i(stuffShareApp.TAG, "masa donasi " + stuffShareApp.getData().getMasaDonasi());
                        if (stuffShareApp.getData().getIduser().equals(sharedPrefManager.getSPUserid()) && stuffShareApp.getData().getMasaDonasi() > 0){
                            Intent goAlertHaveCampaignActivity = new Intent(getApplication(), AlertHaveCampaignActivity.class);
                            startActivity(goAlertHaveCampaignActivity);
                        } else {
                            Intent goAlertQuestionActivity = new Intent(getApplication(), AlertQuestionActivity.class);
                            startActivity(goAlertQuestionActivity);
                        }
                    } else {
                        Intent goAlertQuestionActivity = new Intent(getApplication(), AlertQuestionActivity.class);
                        startActivity(goAlertQuestionActivity);
                    }
                }
                break;
            case R.id.navigation_account:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private void requestPermission () {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    public static void ShowFragment(int resId, Fragment fragment, androidx.fragment.app.FragmentManager fm) {
        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(resId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    long back_pressed;

    @Override
    public void onBackPressed() {
        if (back_pressed + 2000 > System.currentTimeMillis()){
            super.onBackPressed();
        }
        else{
            Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed = System.currentTimeMillis();
    }
}
