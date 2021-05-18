package com.example.personalisedmobilepaindiary.fragments;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.example.personalisedmobilepaindiary.R;

public class AlarmReceiver extends BroadcastReceiver {

    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Alarm! Time to create new pain record", Toast.LENGTH_LONG).show();
    }
}
