package com.stuff.stuffshare.service;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.stuff.stuffshare.MainActivity;
import com.stuff.stuffshare.R;
import com.stuff.stuffshare.StuffShareApp;
import com.stuff.stuffshare.service.BootReceiver;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import es.dmoral.toasty.Toasty;

import static com.stuff.stuffshare.StuffShareApp.notificationId;

public class NoficationService extends Service {

    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    Context context;
    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
//        String input = intent.getStringExtra("inputExtra");
//        createNotificationChannel();

        //do heavy work on a background thread
        //stopSelf();
        handler.post(periodicUpdate);
        return START_NOT_STICKY;
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    Handler handler = new Handler();
    private Runnable periodicUpdate = new Runnable() {
        @Override
        public void run() {
            handler.postDelayed(periodicUpdate, 1000);
//            Intent notificationIntent = new Intent(NoficationService.this, NoficationService.class);
//            PendingIntent pendingIntent = PendingIntent.getActivity(NoficationService.this, 0, notificationIntent, 0);
//            AlarmManager keepAwake = (AlarmManager) getSystemService(ALARM_SERVICE);
//            keepAwake.setExact(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime(), pendingIntent);
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 21);
            calendar.set(Calendar.MINUTE, 50);

//            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//            Intent notificationIntent = new Intent(context, NoficationService.class);
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//            alarmManager.setRepeating(AlarmManager.RTC, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//            long current = System.currentTimeMillis();

            Notification notification = null;
            AlarmManager keepAwake = null;
            PendingIntent pendingIntent = null;

//            if ((current-current%1000)%(1000*10) == 0){
            if (calendar.getTime().compareTo(new Date()) == 0){
                createNotificationChannel();
                calendar.add(Calendar.DAY_OF_MONTH, 1);
                Intent notificationIntent = new Intent(NoficationService.this, NoficationService.class);
                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                pendingIntent = PendingIntent.getActivity(NoficationService.this,
                        0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                notification = new NotificationCompat.Builder(NoficationService.this, CHANNEL_ID)
                    .setContentTitle("Foreground Service")
                    .setContentText("Testing with time")
                    .setSmallIcon(R.drawable.ic_folder_icon)
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .build();
                startForeground(1, notification);
                keepAwake = (AlarmManager) getSystemService(ALARM_SERVICE);
                keepAwake.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
                if (pendingIntent != null) {
//                    boolean alarmUp = (PendingIntent.getBroadcast(getApplicationContext(), 327,
//                            new Intent(getApplicationContext(), NoficationService.class),
//                            PendingIntent.FLAG_NO_CREATE) != null);
                    keepAwake.cancel(pendingIntent);
                    pendingIntent.cancel();
                    handler.removeCallbacks(periodicUpdate);
                }
//                    keepAwake.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
//                    createNotificationChannel();
//                    keepAwake.cancel(pendingIntent);

//                }

//            } else if (calendar.getTime().compareTo(new Date()) > 0){
//                calendar.add(Calendar.DAY_OF_MONTH, 1);
//                Intent notificationIntent = new Intent(NoficationService.this, NoficationService.class);
//                notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                pendingIntent = PendingIntent.getActivity(NoficationService.this,
//                        0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                keepAwake = (AlarmManager) getSystemService(ALARM_SERVICE);
//                keepAwake.cancel(pendingIntent);
            }
        }
    };


    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }
}
