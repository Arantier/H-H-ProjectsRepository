package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.BridgeClasses.TimeInterval;
import ru.android_school.h_h.sevenapp.R;

public class BridgePageActivity extends AppCompatActivity implements TimePickerDialog.Callback {

    public static final int IMAGE_COUNT = 2;
    public static final String INTENT_BRIDGE = "bridge";

    Bridge bridge;
    Toolbar toolbar;
    ViewPager bridgePhotos;
    ViewGroup bridgeBar;
    TextView bridgeDescription;
    ViewGroup reminderButton;

    class PagerAdapter extends FragmentPagerAdapter {

        String photoOpen;
        String photoClosed;


        public PagerAdapter(FragmentManager fm, String photoOpen, String photoClosed) {
            super(fm);
            this.photoOpen = photoOpen;
            this.photoClosed = photoClosed;
        }

        @Override
        public Fragment getItem(int i) {
            if (i == 0) {
                return ImageFragment.newInstance(photoOpen);
            } else {
                return ImageFragment.newInstance(photoClosed);
            }
        }

        @Override
        public int getCount() {
            return IMAGE_COUNT;
        }

    }

    public static String makeMinutesString(Context context, int minutes) {
        if (minutes <= 0) {
            return context.getResources().getString(R.string.button_reminder);
        } else if (minutes < 60) {
            return context.getResources().getQuantityString(R.plurals.minute_plurals, minutes, minutes);
        } else {
            return context.getResources().getQuantityString(R.plurals.hours_plurals, minutes / 60, minutes / 60);
        }
    }

    public void createNotificationAndRefreshButton(int minutesToCall) {
        Calendar timeToCall = BridgeManager.getClosestStart(bridge);
        timeToCall.add(Calendar.MINUTE, -minutesToCall);
        Intent notificationContentIntent = new Intent(this, NotificationReceiver.class);
        /*notificationContentIntent.setAction(NotificationReceiver.SET_NOTIFICATION);
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_TIME, minutesToCall);
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_ID,bridge.getId());
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_NAME,bridge.getName());
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_DESCRIPTION,bridge.getDescription());
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_PHOTO_OPEN,bridge.getPhotoBridgeOpenURL());
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_PHOTO_CLOSE,bridge.getPhotoBridgeClosedURL());
        TimeInterval[] intervals = (TimeInterval[]) bridge.getIntervals().toArray();
        String[] intervalsStrings = new String[intervals.length*2];
        for (int i=0;i<intervalsStrings.length;i++){
            intervalsStrings[i] = intervals[i].getStartString();
            intervalsStrings[i+1] = intervals[i].getEndString();
        }
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_INTERVALS,intervalsStrings);
        PendingIntent intentToSend = PendingIntent.getBroadcast(this,bridge.getId(),notificationContentIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        getSharedPreferences(NotificationReceiver.TIMERS_PREFERENCES, Context.MODE_PRIVATE)
                .edit()
                .putInt(bridge.getId() + "", minutesToCall)
                .apply();
        AlarmManager am = (AlarmManager) getSystemService(ALARM_SERVICE);
        am.set(AlarmManager.RTC,0,intentToSend);*/
        updateButton(minutesToCall);
    }

    @Override
    public void cancel() {
        Intent cancellingIntent = new Intent(NotificationReceiver.REMOVE_NOTIFICATION);
        cancellingIntent.putExtra(NotificationReceiver.INTENT_BRIDGE_ID, bridge.getId());
        sendBroadcast(cancellingIntent);
        updateButton(0);
    }

    void updateButton(int minutesToCall) {
        String buttonText = makeMinutesString(this, minutesToCall).toUpperCase();
        ((TextView) reminderButton.findViewById(R.id.reminderButtonText)).setText(buttonText);
        if (minutesToCall > 0) {
            ((ImageView) bridgeBar.findViewById(R.id.image_isSubscribed)).setImageResource(R.drawable.ic_bell_on);
        } else {
            ((ImageView) bridgeBar.findViewById(R.id.image_isSubscribed)).setImageResource(R.drawable.ic_bell_off);
        }
    }

    void updateStatus() {
        SharedPreferences mapOfTimers = getSharedPreferences(NotificationReceiver.TIMERS_PREFERENCES, Context.MODE_PRIVATE);
        String buttonText;
        if (mapOfTimers.contains(bridge.getId() + "")) {
            int minutesToCall = mapOfTimers.getInt(bridge.getId() + "", 0);
            buttonText = makeMinutesString(this, minutesToCall);
            buttonText = buttonText.toUpperCase();
        } else {
            buttonText = getResources().getString(R.string.button_reminder);
        }
        ((TextView) reminderButton.findViewById(R.id.reminderButtonText)).setText(buttonText);
        BridgeManager.makeBridgeBar(bridge, bridgeBar);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_page);
        toolbar = findViewById(R.id.bridgePageToolbar);
        bridgePhotos = findViewById(R.id.imagePager);
        bridgeBar = findViewById(R.id.bridgeItemIncluded);
        bridgeDescription = findViewById(R.id.bridgeDescription);
        reminderButton = findViewById(R.id.reminderButton);
        bridge = getIntent().getParcelableExtra(INTENT_BRIDGE);
        updateStatus();
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(listener -> BridgePageActivity.this.finish());
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), bridge.getPhotoBridgeOpenURL(), bridge.getPhotoBridgeClosedURL());
        bridgePhotos.setAdapter(adapter);
        BridgeManager.makeBridgeBar(bridge, bridgeBar);
        bridgeDescription.setText(Html.fromHtml(bridge.getDescription()));
        reminderButton.setOnClickListener(view -> {
            Log.i("button", "Button pressed");
            TimePickerDialog.newInstance(bridge.getId(), bridge.getName(), this)
                    .show(getSupportFragmentManager(), "time");
        });
    }
}
