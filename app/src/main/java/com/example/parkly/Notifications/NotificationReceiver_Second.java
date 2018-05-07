package com.example.parkly.Notifications;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

import com.example.parkly.Activity.MainActivity;

/**
 * Created by donvel on 2018-05-07.
 */

public class NotificationReceiver_Second extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(android.R.mipmap.sym_def_app_icon)
                .setContentTitle("Information about parking")
                .setContentText("Your parking time will end after 5 minutes")
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }

}
