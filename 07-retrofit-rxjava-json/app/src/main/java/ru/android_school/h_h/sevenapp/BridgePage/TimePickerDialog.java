package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
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

    interface Callback {
        public void createNotificationAndRefreshButton(int minutesToCall);

        public void cancel();

    }

    public static final String TAG = "time_picker";

    private int bridgeId;
    private String bridgeName;
    private Callback callbackToActivity;
    private int selectedTimeInMinutes;

    public static TimePickerDialog newInstance(int bridgeId, String bridgeName, Callback callbackToActivity) {
        TimePickerDialog instance = new TimePickerDialog();
        instance.bridgeId = bridgeId;
        instance.bridgeName = bridgeName;
        instance.callbackToActivity = callbackToActivity;
        return instance;
    }

    TextView title;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View dialogView = getActivity().getLayoutInflater().inflate(R.layout.dialog_notification, null);
        title = dialogView.findViewById(R.id.title);
        title.setText(bridgeName);
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
        Log.i(TAG, "Bridge's name:" + bridgeName + "\n");
        builder.setView(dialogView);
        builder.setPositiveButton(R.string.dialogPositive, (positiveListener, i) -> {
            callbackToActivity.createNotificationAndRefreshButton(selectedTimeInMinutes);
        }).setNegativeButton(R.string.dialogNegative, (negativeListener, i) -> {
            TimePickerDialog.this.getDialog().cancel();
        });
        PendingIntent notificationStatus = PendingIntent.getBroadcast(getContext(),bridgeId,new Intent(NotificationReceiver.CALL_NOTIFICATION),PendingIntent.FLAG_NO_CREATE);
        if (notificationStatus!=null) {
            builder.setNeutralButton(R.string.dialogNeutral, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    callbackToActivity.cancel();
                }
            });
        }
        return builder.create();
    }

}
