package ru.android_school.h_h.sevenapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Date;

public class BridgePage extends AppCompatActivity {

    public static final String BRIDGE_TAG = "bridge_tag";

    Toolbar toolbar;
    ImageView bridgePhoto;
    ViewGroup bridgeItem;
    TextView bridgeDescription;
    ViewGroup reminderButton;

    protected void setBridgeItem(ViewGroup item, Bridge bridge){
        ((TextView) item.findViewById(R.id.bridgeName)).setText(bridge.name);
        int state = 0;
        //TODO: Замени дефис на тире
        String formattedTime="";
        for (TimeInterval i : bridge.bridgeIntervals){
            formattedTime+=i+"\t";
            int newState = i.whatPosition(new Date());
            if (state<newState){
                state = newState;
            }
        }
        switch (state){
            case (TimeInterval.BRIDGE_CONNECTED):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_normal);
                break;
            case (TimeInterval.BRIDGE_SOON):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_soon);
                break;
            case (TimeInterval.BRIDGE_RAISED):
                ((ImageView) item.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_late);
                break;
        }
        ((TextView) item.findViewById(R.id.bridgeTime)).setText(formattedTime);
        ((ImageView) item.findViewById(R.id.image_isSubscribed)).setImageResource((bridge.isSubscribed ? R.drawable.ic_bell_on : R.drawable.ic_bell_off));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bridge_page);
        toolbar = findViewById(R.id.bridgePageToolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(listener -> BridgePage.this.finish());
        Intent receivedInfo = getIntent();
        Bridge bridge = receivedInfo.getParcelableExtra(BRIDGE_TAG);
        Log.i("page","Bridge has been delivered:\n"+bridge.id+"\n"+bridge.name+"\n"+bridge.description+"\n"+bridge.bridgeIntervals+"\n"+bridge.photoBridgeClosedURL+"\n"+bridge.photoBridgeOpenURL);
        bridgePhoto = findViewById(R.id.bridgePhoto);
        bridgeItem = findViewById(R.id.bridgeItemIncluded);
        bridgeDescription = findViewById(R.id.bridgeDescription);
        reminderButton = findViewById(R.id.reminderButton);
        setBridgeItem(bridgeItem,bridge);
//        bridge.putBridgeOpenImage(getApplicationContext(),bridgePhoto);
        bridgeDescription.setText(bridge.description);
        bridgeDescription.setMovementMethod(new ScrollingMovementMethod());
    }
}
