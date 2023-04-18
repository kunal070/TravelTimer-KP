package com.example.traveltimer2;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.ContextWrapper;
import android.os.Build;

import androidx.core.app.NotificationCompat;

public class NotificationHelper extends ContextWrapper {
    public final static String chanel1ID = "chanel1ID";
    public final static String chanel1NAME = "Chanel 1";
    public final static String chanel2ID = "chanel2ID";
    public final static String chanel2NAME = "Chanel 2";
    private NotificationManager mManager;


    @TargetApi(Build.VERSION_CODES.O)
    public NotificationHelper(Context base) {
        super(base);
        createChanels();
    }


    @TargetApi(Build.VERSION_CODES.O)
    public void createChanels() {
        NotificationChannel chanel1 = new NotificationChannel(chanel1ID, chanel1NAME, NotificationManager.IMPORTANCE_DEFAULT);
        chanel1.enableLights(true);
        chanel1.enableVibration(true);
        chanel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(chanel1);

        NotificationChannel chanel2 = new NotificationChannel(chanel2ID, chanel2NAME, NotificationManager.IMPORTANCE_DEFAULT);
        chanel2.enableLights(true);
        chanel2.enableVibration(true);
        chanel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);

        getManager().createNotificationChannel(chanel2);


    }
    public NotificationManager getManager() {

        mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        return mManager;




    }

    public NotificationCompat.Builder getChanel1Notification() {
        return (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle("Будильник")
                .setContentText("Работает")
                .setSmallIcon(R.drawable.ic_baseline_location_on_24);


    }
    public NotificationCompat.Builder getChane2Notification(String title, String message) {
        return (NotificationCompat.Builder) new NotificationCompat.Builder(getApplicationContext())
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_baseline_location_on_24);


    }
}
