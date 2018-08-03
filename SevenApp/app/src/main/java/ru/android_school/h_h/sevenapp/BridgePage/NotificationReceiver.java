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
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BRIDGE = "bridge";
    public static final String INTENT_TIME = "time";
    public static final String LOG_TAG = "NotificationReceiver";

    private Bridge bridge;
    private int timeToCall;


    @Override
    public void onReceive(Context context, Intent intent){
        Log.i(LOG_TAG, "onReceive");
        Log.i(LOG_TAG, "intent = " + intent);
        Log.i(LOG_TAG, "time = " + intent.getIntExtra(INTENT_TIME,0));
        Log.i(LOG_TAG, "bridge = " + intent.getParcelableExtra(INTENT_BRIDGE));
        Log.i(LOG_TAG, "extras = " + intent.getExtras());
    }
//    @Override
    public void bannedonReceive(Context context, Intent intent) {
        String TAG = "notification_trouble";
        NotificationCompat.Builder builder;
        bridge = intent.getParcelableExtra(INTENT_BRIDGE);
        timeToCall = intent.getIntExtra(INTENT_TIME,0);
        String description = "Вы просили напомнить о мосте " + BridgePage.makeMinutesString(timeToCall) + " до развода моста";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "Нынешняя версия выше 26");
            String channelId = "" + bridge.getId();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, bridge.getName(), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0x3de095);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(context,channelId);
        } else {
            Log.i(TAG, "Нынешняя версия ниже 26");
            builder = new NotificationCompat.Builder(context);
        }
        Intent makeBridgePageIntent = new Intent(context,BridgePage.class);
        intent.putExtra(BridgePage.BRIDGE_TAG,bridge);
        PendingIntent bridgePagePendingIntent = PendingIntent.getActivity(context,bridge.getId(),makeBridgePageIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle(bridge.getName())
                .setContentText(description)
                .setAutoCancel(true)
                .setContentIntent(bridgePagePendingIntent)
                .build();
        notificationManager.notify(bridge.getId(),notification);
    }
}
