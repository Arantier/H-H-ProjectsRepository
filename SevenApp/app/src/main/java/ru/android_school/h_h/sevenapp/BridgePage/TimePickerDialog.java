package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import java.util.Calendar;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.R;

public class TimePickerDialog extends DialogFragment {

    public static final String TAG = "time_picker";

    //TODO: Сделать сохранение мостов в кэш и организовывать всяческие проверки и доступ по id
    private Bridge bridge;
    private Calendar closestStart;
    private int selectedTimeInMinutes;

    public static TimePickerDialog newInstance(Bridge bridge) {
        TimePickerDialog instance = new TimePickerDialog();
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
//            Intent notificationContentIntent = new Intent(getContext(),NotificationReceiver.class);
            Intent notificationContentIntent = new Intent("Boop");
            notificationContentIntent.setClass(getContext(),NotificationReceiver.class);
            notificationContentIntent.putExtra(NotificationReceiver.INTENT_TIME,selectedTimeInMinutes);
            notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE,bridge);
            //Этот закрепляем за алярмой
            PendingIntent notificationCallIntent = PendingIntent.getBroadcast(getContext(),bridge.getId(),notificationContentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager manager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
            manager.set(AlarmManager.RTC,timeToCall,notificationCallIntent);
        }).setNegativeButton(R.string.dialogNegative, (negativeListener, i) -> {
            TimePickerDialog.this.getDialog().cancel();
        });
        return builder.create();
    }

}
