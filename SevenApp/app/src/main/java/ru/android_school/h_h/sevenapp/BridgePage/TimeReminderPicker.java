package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.R;

public class TimeReminderPicker extends DialogFragment {

    public static final String TAG = "time_picker";

    //TODO: Сделать сохранение мостов в кэш и организовывать всяческие проверки и доступ по id
    private Bridge bridge;
    private Calendar closestStart;
    private int selectedTimeInMinutes;

    public static TimeReminderPicker newInstance(Bridge bridge) {
        TimeReminderPicker instance = new TimeReminderPicker();
        instance.bridge = bridge;
        instance.closestStart = new BridgeManager(bridge).getClosestStart();
        return instance;
    }

    TextView title;

    public void createNotification(Bridge bridge, String description) {
        String TAG = "notification_trouble";
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "Нынешняя версия выше 26");
            String channelId = "" + bridge.getId();
            NotificationChannel notificationChannel = new NotificationChannel(channelId, bridge.getName(), NotificationManager.IMPORTANCE_DEFAULT);
            notificationChannel.setDescription(description);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(0x3de095);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(getContext(),channelId);
        } else {
            Log.i(TAG, "Нынешняя версия ниже 26");
            builder = new NotificationCompat.Builder(getContext());
        }
        Intent intent = new Intent(getContext(),BridgePage.class);
        intent.putExtra(BridgePage.BRIDGE_TAG,bridge);
        PendingIntent makeBridgePageIntent = PendingIntent.getActivity(getContext(),bridge.getId(),intent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = builder.setSmallIcon(R.drawable.ic_adb_black_24dp)
                .setContentTitle(bridge.getName())
                .setContentText(description)
                .setAutoCancel(true)
                .setContentIntent(makeBridgePageIntent)
                .build();
        notificationManager.notify(bridge.getId(),notification);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_notification, null);
        title = dialogView.findViewById(R.id.title);
        title.setText(bridge.getName());
        NumberPicker numberPicker = dialogView.findViewById(R.id.numberPicker);
        String times[] = {"15 мин", "30 мин", "45 мин", "1 час", "2 часа"};
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(times.length - 1);
        numberPicker.setDisplayedValues(times);
        numberPicker.setWrapSelectorWheel(true);
        numberPicker.setOnValueChangedListener((numberPickerEvent, oldVal, newVal) -> {
            selectedTimeInMinutes = ((newVal < 3) ? (newVal + 1) * 15 : (newVal - 2) * 60);
        });
        selectedTimeInMinutes = 15;
        Log.i(TAG, "Bridge's name:" + bridge.getName() + "\n");
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.dialogPositive, (positiveListener, i) -> {
            closestStart.add(Calendar.MINUTE, -selectedTimeInMinutes);
//            long time = closestStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            long timeToCall = Calendar.getInstance().getTimeInMillis() + 5000;
            Log.i(TAG, "Время выставлено:" + timeToCall + "\nНынешнее время:" + Calendar.getInstance().getTimeInMillis());
            //В этот интент размещаем данные и пункт назначения
            Log.i(TAG,"Отправляется мост и время:\nВремя"+selectedTimeInMinutes+"\nМост:"+bridge);
            Intent notificationContentIntent = new Intent(getContext(),NotificationReceiver.class);
            notificationContentIntent.putExtra(NotificationReceiver.INTENT_TIME,selectedTimeInMinutes);
            notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE,bridge);
            //Этот закрепляем за алярмой
            PendingIntent notificationCallIntent = PendingIntent.getBroadcast(getContext(),bridge.getId(),notificationContentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC,timeToCall,notificationCallIntent);
        }).setNegativeButton(R.string.dialogNegative, (negativeListener, i) -> {
            TimeReminderPicker.this.getDialog().cancel();
        });
        return builder.create();
    }

}
