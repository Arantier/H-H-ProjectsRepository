package ru.android_school.h_h.sevenapp;

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

import ru.android_school.h_h.sevenapp.support_classes.Bridge;

public class TimeReminderPicker extends DialogFragment {

    public static final String TAG = "time_picker";

    private Integer selectedTimeInMinutes;
    private Bridge bridge;
    BridgePage.ChangeableButton changeAbleButton;

    public static TimeReminderPicker newInstance(Bridge bridge, BridgePage.ChangeableButton changeAbleButton) {
        TimeReminderPicker instance = new TimeReminderPicker();
        instance.selectedTimeInMinutes = 15;
        instance.changeAbleButton = changeAbleButton;
        instance.bridge = bridge;
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
            Log.i(TAG, "Selected time:" + selectedTimeInMinutes);
            bridge.timeToRemindInMinutes = selectedTimeInMinutes;
            changeAbleButton.setTextOnButton(bridge.timeToRemindInMinutes);
            Calendar closestStart = ((Calendar) bridge.getClosestStart().clone());
            closestStart.add(Calendar.MINUTE,-selectedTimeInMinutes);
            AlarmManager am = (AlarmManager) (this.getContext().getSystemService(Context.ALARM_SERVICE));
            Intent reminder = new Intent(getContext(), NotificationReceiver.class);
            reminder.putExtra(NotificationReceiver.INTENT_BRIDGE_TAG, bridge)
                    .putExtra(NotificationReceiver.INTENT_TIME, selectedTimeInMinutes);
            PendingIntent pendingReminder = PendingIntent.getBroadcast(getContext(), 0, reminder, 0);
            am.set(AlarmManager.RTC, closestStart.getTimeInMillis(), pendingReminder);
        }).setNegativeButton(R.string.dialogNegative, (negativeListener, i) -> {
            TimeReminderPicker.this.getDialog().cancel();
        });
        return builder.create();
    }

}
