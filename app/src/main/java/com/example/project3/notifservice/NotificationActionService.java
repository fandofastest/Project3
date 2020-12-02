package com.example.project3.notifservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import static com.example.project3.utils.Static.ACTIONNAME;
import static com.example.project3.utils.Static.INTENTFILTERNOTIF;

public class NotificationActionService extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.sendBroadcast(new Intent(INTENTFILTERNOTIF)
        .putExtra(ACTIONNAME, intent.getAction()));
    }
}
