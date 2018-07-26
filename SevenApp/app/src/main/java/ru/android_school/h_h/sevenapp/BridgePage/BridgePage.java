package ru.android_school.h_h.sevenapp.BridgePage;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.NotificationCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeManager;
import ru.android_school.h_h.sevenapp.MainActivity.ListFragment;
import ru.android_school.h_h.sevenapp.R;

public class BridgePage extends AppCompatActivity {

    public static final int IMAGE_COUNT = 2;
    public static final String BRIDGE_TAG = "bridge";

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

    //TODO: Не сделана связь с кнопкой
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_page);
        toolbar = findViewById(R.id.bridgePageToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(listener -> BridgePage.this.finish());
        Intent receivedInfo = getIntent();
        Bridge bridge = receivedInfo.getParcelableExtra(BRIDGE_TAG);
        bridgePhotos = findViewById(R.id.imagePager);
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(), bridge.getPhotoBridgeOpenURL(), bridge.getPhotoBridgeClosedURL());
        bridgePhotos.setAdapter(adapter);
        BridgeManager bridgeManager = new BridgeManager(bridge);
        bridgeBar = findViewById(R.id.bridgeItemIncluded);
        bridgeDescription = findViewById(R.id.bridgeDescription);
        reminderButton = findViewById(R.id.reminderButton);
        bridgeManager.makeBridgeBar(bridgeBar);
        bridgeDescription.setText(Html.fromHtml(bridge.getDescription()));
        reminderButton.setOnClickListener(view -> {
            Log.i("button","Button pressed");
            String description = "Вы просили напомнить о мосте "+BridgePage.makeMinutesString(0)+" до развода моста";
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.ic_adb_black_24dp)
                    .setContentTitle(bridge.getName())
                    .setContentInfo(description);
            NotificationManager notificationManager =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notificationBuilder.build());
            /*TimeReminderPicker.newInstance(bridge)
                    .show(getSupportFragmentManager(), "time");*/
        });
    }
}
