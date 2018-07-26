package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.NotificationCompat;
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
        Log.i(TAG, "Bridge's name:" + bridge.getName() + "\n");
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.dialogPositive, (positiveListener, i) -> {
            closestStart.add(Calendar.MINUTE,-selectedTimeInMinutes);
//            long time = closestStart.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
            long time = Calendar.getInstance().getTimeInMillis()+5000;
            Log.i(TAG,"Время выставлено:"+time+"\nНынешнее время:"+Calendar.getInstance().getTimeInMillis());
            /*Intent notificationIntent = new Intent(getContext(),NotificationReceiver.class);
            notificationIntent.putExtra(NotificationReceiver.INTENT_BRIDGE,bridge.getName());
            notificationIntent.putExtra(NotificationReceiver.INTENT_TIME,selectedTimeInMinutes);*/
//            PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),bridge.getId(),notificationIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            /*AlarmManager alarmManager = ((AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE));
            alarmManager.set(AlarmManager.RTC,time,pendingIntent);*/
            String description = "Вы просили напомнить о мосте "+BridgePage.makeMinutesString(selectedTimeInMinutes)+" до развода моста";
            //Не показывается

            /*NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getContext())
                    .setSmallIcon(R.drawable.ic_adb_black_24dp)
                    .setContentTitle(bridge.getName())
                    .setContentInfo(description);
            NotificationManager notificationManager =
                    (NotificationManager) getContext().getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());*/
        }).setNegativeButton(R.string.dialogNegative, (negativeListener, i) -> {
            TimeReminderPicker.this.getDialog().cancel();
        });
        return builder.create();
    }

}
