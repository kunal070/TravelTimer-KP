package com.example.traveltimer2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        // Do something when the alarm goes off
        Toast.makeText(context, "Alarm triggered!", Toast.LENGTH_SHORT).show();
    }
}