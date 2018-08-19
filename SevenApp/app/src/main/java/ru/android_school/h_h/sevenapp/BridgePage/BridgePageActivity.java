package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
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
import android.widget.TextView;

import java.util.Calendar;

import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeDatabase;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.R;

public class BridgePageActivity extends AppCompatActivity implements TimePickerDialog.Callback {

    public static final int IMAGE_COUNT = 2;
    public static final String BRIDGE_INTENT = "bridge";
    public static final String START_WITH_BRIDGE = "bridge_received";
    public static final String START_WITH_ID = "bridge_get_from_database";
    public static final String BRIDGE_ID_INTENT = "bridge_id";

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

    Bridge bridge;
    Toolbar toolbar;
    ViewPager bridgePhotos;
    ViewGroup bridgeBar;
    TextView bridgeDescription;
    ViewGroup reminderButton;

    public static String makeMinutesString(int minutes) {
        //Тут такое дело
        //По идее этот метод должен работать независимо от класса, т.к он всего лишь делает строку
        //Но тут возникает проблемка сс контекстом
        //Так что я использую обычные строки, если подскажешь как это исправить  - буду признателен
        String result = "";
        if (minutes != 0) {
            result = "за ";
            if (minutes < 60) {
                result += minutes;
                result += " минут";
            } else {
                int timeToRemindInHours = minutes / 60;
                result += timeToRemindInHours;
                result += " час";
                if (((timeToRemindInHours % 10) > 1) && ((timeToRemindInHours % 10) < 5)) {
                    result += "а";
                } else if (timeToRemindInHours > 4) {
                    result += "ов";
                }
            }
        } else {
            result = "Напомнить";
        }
        return result;
    }

    public void createNotificationAndRefreshButton(int minutesToCall) {
        String logTag = "Notification setting";
        Calendar timeToCall = BridgeManager.getClosestStart(bridge);
        timeToCall.add(Calendar.MINUTE, -minutesToCall);
//            long time = timeToCall.getTimeInMillis() - Calendar.getInstance().getTimeInMillis();
        long timeToCallMillis = Calendar.getInstance().getTimeInMillis() + 5000;
        Log.i(logTag, "Время выставлено:" + timeToCallMillis + "\nНынешнее время:" + Calendar.getInstance().getTimeInMillis());
        //В этот интент размещаем данные и пункт назначения
        Log.i(logTag, "Отправляется мост и время:\nВремя" + minutesToCall + "\nМост:" + bridge);
//        Intent notificationContentIntent = new Intent("ru.android_school.h_h.sevenapp.MAKE_NOTIFICATION");
        Intent notificationContentIntent = new Intent(BridgePageActivity.this,NotificationReceiver.class);
        notificationContentIntent.setAction("ru.android_school.h_h.sevenapp.MAKE_NOTIFICATION");
        notificationContentIntent.putExtra("csc",55);
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_TIME, minutesToCall);
        notificationContentIntent.putExtra(NotificationReceiver.INTENT_BRIDGE, bridge);
        //Этот закрепляем за алярмой
        PendingIntent notificationCallIntent = PendingIntent.getBroadcast(this, bridge.getId(), notificationContentIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.set(AlarmManager.RTC, 0, notificationCallIntent);
        ((TextView) reminderButton.findViewById(R.id.reminderButtonText)).setText(getResources().getString(R.string.notificationSet));
    }

    //TODO: Не сделана связь с кнопкой
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_page);
        Intent receivedInfo = getIntent();
        if (receivedInfo.getAction()==START_WITH_BRIDGE){
            bridge = receivedInfo.getParcelableExtra(BRIDGE_INTENT);
        } else {
            int bridgeId = receivedInfo.getIntExtra(BRIDGE_ID_INTENT,-1);
            BridgeDatabase db = Room.databaseBuilder(this, BridgeDatabase.class, "BridgeDatabase")
                    .build();
            db.bridgeDao()
                    .get(bridgeId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<Bridge>() {
                        @Override
                        public void accept(Bridge bridge) throws Exception {
                            BridgePageActivity.this.bridge = bridge;
                        }
                    });
        }
        toolbar = findViewById(R.id.bridgePageToolbar);
        bridgePhotos = findViewById(R.id.imagePager);
        bridgeBar = findViewById(R.id.bridgeItemIncluded);
        bridgeDescription = findViewById(R.id.bridgeDescription);
        reminderButton = findViewById(R.id.reminderButton);
    }

    @Override
    protected void onStart() {
        super.onStart();
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(listener -> BridgePageActivity.this.finish());
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), bridge.getPhotoBridgeOpenURL(), bridge.getPhotoBridgeClosedURL());
        bridgePhotos.setAdapter(adapter);
        BridgeManager bridgeManager = new BridgeManager(bridge);
        bridgeManager.makeBridgeBar(bridgeBar);
        bridgeDescription.setText(Html.fromHtml(bridge.getDescription()));
        reminderButton.setOnClickListener(view -> {
            Log.i("button", "Button pressed");
            TimePickerDialog.newInstance(bridge.getName(), this)
                    .show(getSupportFragmentManager(), "time");
        });
    }
}
