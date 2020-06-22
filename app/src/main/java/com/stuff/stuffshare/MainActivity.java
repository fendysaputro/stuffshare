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
import android.view.MenuItem;
import android.view.View;

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
        setContentView(R.layout.activity_main);

        requestPermission();

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
        getCampaign.execute(stuffShareApp.HOST + stuffShareApp.CAMPAIGN, "GET");
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
//                            long noOfDaysBetween = ChronoUnit.DAYS.between(dateBefore, dateAfter);
                            long noOfDaysBetween = dateAfter.until(dateBefore, org.threeten.bp.temporal.ChronoUnit.DAYS);
//                            long result = l2.until(l1, ChronoUnit.DAYS);
                            String dateString = DateFormat.format("yyyy-MM-dd", new Date(noOfDaysBetween)).toString();
                            String timeStamp = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
                            LocalDate dayNow = LocalDate.parse(timeStamp);
                            LocalDate dateMass = LocalDate.parse(dateString);
                            long massDonation = dayNow.until(dateAfter, ChronoUnit.DAYS);
                            int ms = (int)massDonation;
                            data.setMasaDonasi(ms);
                            stuffShareApp.setData(data);
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
//                fragment = new AddDonationFragment();
//                Intent intent = new Intent(getApplication(), AlertQuestionActivity.class);
//                startActivity(intent);
                if (stuffShareApp.getData().getIduser().equals(sharedPrefManager.getSPUserid()) && stuffShareApp.getData().getMasaDonasi() > 0){
                    Intent goAlertHaveCampaignActivity = new Intent(getApplication(), AlertHaveCampaignActivity.class);
                    startActivity(goAlertHaveCampaignActivity);
                } else {
                    Intent goAlertQuestionActivity = new Intent(getApplication(), AlertQuestionActivity.class);
                    startActivity(goAlertQuestionActivity);
                }
                break;
            case R.id.navigation_account:
                fragment = new ProfileFragment();
                break;
        }
        return loadFragment(fragment);
    }

    private void requestPermission () {
        if (ContextCompat.checkSelfPermission(getApplication(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_NETWORK_STATE}, 3);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_NETWORK_STATE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CHANGE_NETWORK_STATE}, 4);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.INTERNET)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.INTERNET}, 5);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 6);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA}, 7);
        }

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.RECORD_AUDIO}, 8);
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.READ_EXTERNAL_STORAGE}, 9);
        }
    }

    public static void ShowFragment(int resId, Fragment fragment, androidx.fragment.app.FragmentManager fm) {
        final FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(resId, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
