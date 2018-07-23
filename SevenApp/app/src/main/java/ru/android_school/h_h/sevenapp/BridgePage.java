package ru.android_school.h_h.sevenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import ru.android_school.h_h.sevenapp.support_classes.Bridge;
import ru.android_school.h_h.sevenapp.support_classes.TimeInterval;

public class BridgePage extends AppCompatActivity {

    public static final String BRIDGE_TAG = "bridge_tag";
    public static final int RESULT_KEY = 1;

    Toolbar toolbar;
    ImageView bridgePhoto;
    ViewGroup bridgeItem;
    TextView bridgeDescription;
    ViewGroup reminderButton;

    interface ChangeableButton {
        public void setTextOnButton(int minutes);
    }

    protected void setBridgeItem(ViewGroup item, Bridge bridge) {
        ((TextView) item.findViewById(R.id.bridgeName)).setText(bridge.getName());
        int state = 0;
        //TODO: Замени дефис на тире
        String formattedTime = "";
        for (TimeInterval interval : bridge.bridgeIntervals) {
            formattedTime += interval + "\t";
        }
        switch (bridge.currentBridgeState()) {
            case (Bridge.BRIDGE_CONNECTED):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_normal);
                break;
            case (Bridge.BRIDGE_SOON):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_soon);
                break;
            case (Bridge.BRIDGE_RAISED):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_late);
                break;
        }
        ((TextView) item.findViewById(R.id.bridgeTime)).setText(formattedTime);
        ((ImageView) item.findViewById(R.id.image_isSubscribed)).setImageResource(((bridge.timeToRemindInMinutes != Bridge.NO_REMIND) ? R.drawable.ic_bell_on : R.drawable.ic_bell_off));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bridge_page);
        toolbar = findViewById(R.id.bridgePageToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(listener -> BridgePage.this.finish());
        Intent receivedInfo = getIntent();
        Bridge bridge = receivedInfo.getParcelableExtra(BRIDGE_TAG);
//        Ошибка с интервалами
//        Log.i("page","Bridge has been delivered:\n"+bridge.id+"\n"+bridge.name+"\n"+bridge.description+"\n"+bridge.bridgeIntervals+"\n"+bridge.photoBridgeClosedURL+"\n"+bridge.photoBridgeOpenURL);
        bridgePhoto = findViewById(R.id.bridgePhoto);
        bridgeItem = findViewById(R.id.bridgeItemIncluded);
        bridgeDescription = findViewById(R.id.bridgeDescription);
        reminderButton = findViewById(R.id.reminderButton);
        if (bridge.timeToRemindInMinutes != Bridge.NO_REMIND) {
            TextView reminderButtonText = reminderButton.findViewById(R.id.reminderButtonText);
            String buttonText = getResources().getString(R.string.button_reminder_active_start);
            if (bridge.timeToRemindInMinutes < 60) {
                buttonText += bridge.timeToRemindInMinutes;
                buttonText += getResources().getString(R.string.button_reminder_active_end_minutes);
            } else {
                int timeToRemindInHours = bridge.timeToRemindInMinutes / 60;
                buttonText += timeToRemindInHours;
                buttonText += getResources().getString(R.string.button_reminder_active_end_hour);
                if (((timeToRemindInHours % 10) > 1) && ((timeToRemindInHours % 10) < 5)) {
                    buttonText += getResources().getString(R.string.button_reminder_active_end_suffix_2_3_4);
                } else if (timeToRemindInHours > 4) {
                    buttonText += getResources().getString(R.string.button_reminder_active_end_suffix_others);
                }
            }
            reminderButtonText.setText(buttonText);
        }
        bridge.putBridgeOpenImage(BridgePage.this,bridgePhoto);
        setBridgeItem(bridgeItem, bridge);
        bridgeDescription.setText(Html.fromHtml(bridge.getDescription()));
        reminderButton.setOnClickListener(listener -> {
            TimeReminderPicker.newInstance(bridge, minutes -> {
                TextView reminderButtonText = reminderButton.findViewById(R.id.reminderButtonText);
                if (minutes != Bridge.NO_REMIND) {
                    String buttonText = getResources().getString(R.string.button_reminder_active_start);
                    if (minutes < 60) {
                        buttonText += minutes;
                        buttonText += getResources().getString(R.string.button_reminder_active_end_minutes);
                    } else {
                        int timeToRemindInHours = minutes / 60;
                        buttonText += timeToRemindInHours;
                        buttonText += getResources().getString(R.string.button_reminder_active_end_hour);
                        if (((timeToRemindInHours % 10) > 1) && ((timeToRemindInHours % 10) < 5)) {
                            buttonText += getResources().getString(R.string.button_reminder_active_end_suffix_2_3_4);
                        } else if (timeToRemindInHours > 4) {
                            buttonText += getResources().getString(R.string.button_reminder_active_end_suffix_others);
                        }
                    }
                    reminderButtonText.setText(buttonText);
                } else {
                    reminderButtonText.setText(getResources().getString(R.string.button_reminder));
                }
                Intent resultBridge = new Intent();
                resultBridge.putExtra(Bridge.TAG,bridge);
                setResult(RESULT_OK,resultBridge);
            })
                    .show(getSupportFragmentManager(), "time");
        });
    }
}
