package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.TreeMap;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.R;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BRIDGE = "bridge";
    public static final String INTENT_BRIDGE_ID = "bridge_id";
    public static final String INTENT_BRIDGE_NAME = "bridge_name";
    public static final String INTENT_BRIDGE_DESCRIPTION = "bridge_descrtiption";
    public static final String INTENT_BRIDGE_INTERVALS = "bridge_intervals";
    public static final String INTENT_BRIDGE_PHOTO_OPEN = "bridge_photo_open";
    public static final String INTENT_BRIDGE_PHOTO_CLOSE = "bridge_photo_close";
    public static final String INTENT_BRIDGE_LATITUDE = "bridge_latitude";
    public static final String INTENT_BRIDGE_LONGTITUDE = "bridge_longtitude";
    public static final String INTENT_TIME = "time";

    public static final String MAKE_NOTIFICATION = "ru.android_school.h_h.sevenapp.make_notification";
    public static final String CALL_NOTIFICATION = "ru.android_school.h_h.sevenapp.call_notification";
    public static final String SET_NOTIFICATION = "ru.android_school.h_h.sevenapp.set_notification";
    public static final String REMOVE_NOTIFICATION = "ru.android_school.h_h.sevenapp.remove_notification";

    public static final String TIMERS_PREFERENCES = "ru.android_school.h_h.sevenapp.timers_preferences";
    public static final String LOG_TAG = "NotificationReceiver";

    TreeMap<Integer, Bridge> mapOfBridges = new TreeMap<>();
    SharedPreferences mapOfMinutes;

    public void createNotification(Context context, Bridge bridge, int minutesToCall) {
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        String description = "Вы просили напомнить о мосте " + BridgePageActivity.makeMinutesString(context, minutesToCall) + " до развода моста";
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
        intent.putExtra(BridgePageActivity.INTENT_BRIDGE, bridge);
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
        if (mapOfMinutes == null) {
            mapOfMinutes = context.getSharedPreferences(TIMERS_PREFERENCES, Context.MODE_PRIVATE);
        }
        String action = intent.getAction();
        switch (action) {
            case MAKE_NOTIFICATION: {
                Bridge bridge = intent.getParcelableExtra(INTENT_BRIDGE);
                int minutesToCall = intent.getIntExtra(INTENT_TIME, -1);
                if (minutesToCall != -1) {
                    mapOfMinutes.edit()
                            .putInt(bridge.getId() + "", minutesToCall)
                            .apply();
                }
                mapOfBridges.put(bridge.getId(), bridge);
                Intent notificationIntent = new Intent(CALL_NOTIFICATION);
                notificationIntent.putExtra(INTENT_BRIDGE_ID, bridge.getId());
                notificationIntent.putExtra(INTENT_TIME, minutesToCall);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, bridge.getId(), notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
                Calendar timeToCall = BridgeManager.getClosestStart(bridge);
                timeToCall.add(Calendar.MINUTE, -minutesToCall);
                long timeToCallMillis = timeToCall.getTimeInMillis();
//                long timeToCallMillis = Calendar.getInstance().getTimeInMillis() + 5000;
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC, timeToCallMillis, pendingIntent);
            }
            break;
            case CALL_NOTIFICATION: {
                int bridgeId = intent.getIntExtra(INTENT_BRIDGE_ID, -1);
                int minutesToCall = intent.getIntExtra(INTENT_TIME, -1);
                mapOfMinutes.edit()
                        .remove(bridgeId + "")
                        .apply();
                Bridge bridge = mapOfBridges.get(bridgeId);
                createNotification(context, bridge, minutesToCall);
                mapOfBridges.remove(bridgeId);
            }
            break;
            case REMOVE_NOTIFICATION: {
                int bridgeId = intent.getIntExtra(INTENT_BRIDGE_ID, -1);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, bridgeId, new Intent(CALL_NOTIFICATION), PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                mapOfBridges.remove(bridgeId);
                mapOfMinutes.edit()
                        .remove(bridgeId + "")
                        .apply();
            }
            break;
            case SET_NOTIFICATION: {
                Bridge receivedBridge = new Bridge(intent.getIntExtra(INTENT_BRIDGE_ID, -1),
                        intent.getStringExtra(INTENT_BRIDGE_NAME),
                        intent.getStringExtra(INTENT_BRIDGE_DESCRIPTION),
                        intent.getStringArrayExtra(INTENT_BRIDGE_INTERVALS),
                        intent.getStringExtra(INTENT_BRIDGE_PHOTO_OPEN),
                        intent.getStringExtra(INTENT_BRIDGE_PHOTO_CLOSE),
                        intent.getDoubleExtra(INTENT_BRIDGE_LATITUDE, -1.0),
                        intent.getDoubleExtra(INTENT_BRIDGE_LONGTITUDE, -1.0));
                int minutesToCall = intent.getIntExtra(INTENT_TIME, -1);
                createNotification(context,receivedBridge,minutesToCall);
                mapOfMinutes.edit()
                        .remove(receivedBridge.getId()+"")
                        .apply();
            }
            break;
            default:
                break;
        }
    }

}
