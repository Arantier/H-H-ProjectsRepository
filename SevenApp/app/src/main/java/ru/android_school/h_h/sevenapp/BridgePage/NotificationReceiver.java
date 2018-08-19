package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BRIDGE = "bridge";
    public static final String INTENT_TIME = "time";
    public static final String LOG_TAG = "NotificationReceiver";

    private Bridge bridge;
    private int timeToCall;

    public void createNotification(Context context) {
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String description = "Вы просили напомнить о мосте " + BridgePageActivity.makeMinutesString(timeToCall) + " до развода моста";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(LOG_TAG, "Нынешняя версия выше 26");
            String channelId = "" + bridge.getId();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, bridge.getName(), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0x3de095);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            Log.i(LOG_TAG, "Нынешняя версия ниже 26");
            builder = new NotificationCompat.Builder(context);
        }
        Intent intent = new Intent(context, BridgePageActivity.class);
        intent.putExtra(BridgePageActivity.BRIDGE_INTENT, bridge);
        PendingIntent makeBridgePageIntent = PendingIntent.getActivity(context, bridge.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle(bridge.getName())
                .setContentText(description)
                .setAutoCancel(true)
                .setContentIntent(makeBridgePageIntent)
                .build();
        notificationManager.notify(bridge.getId(), notification);
    }

    public void createDebugNotification(Context context) {
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String description = "Это отладочная нотификация, которая говорит о том, что принятый интент был сожран Ктулху";
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(LOG_TAG, "Нынешняя версия выше 26");
            String channelId = "debug_notification";
            NotificationChannel notificationChannel = new NotificationChannel(channelId, "Отладочная нотификация", NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0xFF0000);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context, channelId);
        } else {
            Log.i(LOG_TAG, "Нынешняя версия ниже 26");
            builder = new NotificationCompat.Builder(context);
        }
        Notification notification = builder.setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle("Отладочная нотификация")
                .setContentText(description)
                .setAutoCancel(true)
                .build();
        notificationManager.notify(0, notification);
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Log.i("csc","I'm working");
        switch (action) {
            case "ru.android_school.h_h.sevenapp.MAKE_NOTIFICATION":
                timeToCall = intent.getIntExtra(INTENT_TIME, -1);
                bridge = intent.getParcelableExtra(INTENT_BRIDGE);
                Log.i(LOG_TAG, "onReceive");
                Log.i(LOG_TAG, "action = " + intent.getAction());
                Log.i(LOG_TAG, "time = " + timeToCall);
                Log.i(LOG_TAG, "bridge = " + bridge);
                if ((timeToCall < 0) || (bridge == null)) {
                    Log.i(LOG_TAG, "Intent extras has lost");
                    createDebugNotification(context);
                } else {
                    Log.i(LOG_TAG, "All working good");
                    createNotification(context);
                }
                break;
            default:
                break;
        }
    }

}
