package com.example.parkly.Notifications_lt;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;

import com.example.parkly.R;

public class NotificationReceiver_First_Lt extends BroadcastReceiver{

    @Override
    public void onReceive(Context context, Intent intent) {

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSound(alarmSound)
                .setSmallIcon(android.R.drawable.ic_popup_reminder)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .setContentTitle("Info apie stovėjimą")
                .setContentText("Laikas pasibaigs už 10 min")
                .setAutoCancel(true);

        notificationManager.notify(100, builder.build());
    }
}

