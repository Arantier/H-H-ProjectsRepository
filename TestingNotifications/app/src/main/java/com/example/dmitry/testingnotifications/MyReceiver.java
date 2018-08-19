package com.example.dmitry.testingnotifications;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

public class MyReceiver extends BroadcastReceiver {
    public static final String STRING_EXTRA = "not_string";
    public static final String SEND_NOTIFICATION_STRING = "com.example.dmitry.testingnotifications.notification_string";
    public static final String SEND_NOTIFICATION_PARCELABLE = "com.example.dmitry.testingnotifications.notification_parcel";
    public static final String PARCELABLE_EXTRA = "not_parcelable";

    int id = 1;

    public void makeNotification(Context context, String title, String text, int id) {
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channelId = "" + id;
            NotificationChannel notificationChannel = new NotificationChannel(channelId, ""+id, NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription("Testing notification");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0xFF0000);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            builder = new NotificationCompat.Builder(context);
        }
        builder.setSmallIcon(R.drawable.ic_notification_icon);
        if ((title != null) && (text != null)) {
            builder.setContentTitle(title)
                    .setContentText(text);
        } else {
            builder.setContentTitle("Error!")
                    .setContentText("Data has lost");
        }
        notificationManager.notify(id, builder.build());
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case (SEND_NOTIFICATION_STRING):
                String receivedString = intent.getStringExtra(STRING_EXTRA);
                makeNotification(context, "Debug notification №" + id++, receivedString, id);
                break;
            case (SEND_NOTIFICATION_PARCELABLE):
                ParcelableClass pc = intent.getParcelableExtra(PARCELABLE_EXTRA);
                String title = pc.title;
                String text = pc.text;
                makeNotification(context, title, text, id);
                break;
            default:
                break;
        }
    }
}
