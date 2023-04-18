package com.example.traveltimer2;

import android.app.Service;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.util.Log;

public class RingtonePlayingService extends Service {
    private static final String TAG = RingtonePlayingService.class.getSimpleName();
    private static final String URI_BASE = RingtonePlayingService.class.getName() + ".";
    public static final String ACTION_DISMISS = URI_BASE + "ACTION_DISMISS";

    private Ringtone ringtone;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");

        if (intent == null) {
            Log.d(TAG, "The intent is null.");
            return START_REDELIVER_INTENT;
        }

        Uri alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (alarmUri == null) {
            alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        }
        ringtone = RingtoneManager.getRingtone(this, alarmUri);
        ringtone.play();


        String action = intent.getAction();

        if (ACTION_DISMISS.equals(action))
            dismissRingtone();


        return START_NOT_STICKY;
    }

    public void dismissRingtone() {
        Intent i = new Intent(this, RingtonePlayingService.class);
        stopService(i);
    }

    @Override
    public void onDestroy() {
        ringtone.stop();
    }
}