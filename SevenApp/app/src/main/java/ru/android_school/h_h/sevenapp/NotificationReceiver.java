package ru.android_school.h_h.sevenapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

import ru.android_school.h_h.sevenapp.support_classes.Bridge;

public class NotificationReceiver extends BroadcastReceiver {

    public static final String INTENT_BRIDGE_TAG = "bridge";
    public static final String INTENT_TIME = "time";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bridge bridge = intent.getParcelableExtra(INTENT_BRIDGE_TAG);
        int timeInMinutes = intent.getIntExtra(INTENT_TIME, 0);
        String description = context.getResources().getString(R.string.notification_start) + timeInMinutes;
        if (timeInMinutes < 60) {
            description += context.getResources().getString(R.string.notification_end_minutes);
        } else {
            description += context.getResources().getString(R.string.notification_end_hours);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle(bridge.getName())
                .setContentInfo(description);
        Intent showBridgeIntent = new Intent(context, BridgePage.class);
        showBridgeIntent.putExtra(BridgePage.BRIDGE_TAG, bridge);

        //Наглый грабёж из Android Development
        //Надеюсь он мне ещё поможет

        // The stack builder object will contain an artificial back stack for the
        // started Activity.
        // This ensures that navigating backward from the Activity leads out of
        // your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
        // Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(BridgePage.class);
        // Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(showBridgeIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        911,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        notificationBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(112,notificationBuilder.build());
    }
}
