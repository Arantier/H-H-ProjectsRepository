package ru.android_school.h_h.sevenapp.BridgeClasses;

import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Calendar;

import ru.android_school.h_h.sevenapp.BridgePage.NotificationReceiver;
import ru.android_school.h_h.sevenapp.R;

public class BridgeManager {

    private Bridge bridge;

    public BridgeManager(Bridge bridge){
        this.bridge = bridge;
    }

    public int currentBridgeState(){
        int bridgeState = Bridge.BRIDGE_CONNECTED;
        for (TimeInterval interval : bridge.getIntervals()){
            int intervalState = interval.currentState();
            int possibleBridgeState;
            switch (intervalState){
                case (TimeInterval.MORE_THAN_HOUR) :
                    possibleBridgeState = Bridge.BRIDGE_CONNECTED;
                    break;
                case (TimeInterval.LESSER_THAN_HOUR) :
                    possibleBridgeState = Bridge.BRIDGE_SOON;
                    break;
                default:
                    possibleBridgeState = Bridge.BRIDGE_RAISED;
                    break;
            }
            if (possibleBridgeState>bridgeState) {
                bridgeState = possibleBridgeState;
            }
        }
        return bridgeState;
    }

    public static int currentBridgeState(Bridge bridge){
        int bridgeState = Bridge.BRIDGE_CONNECTED;
        for (TimeInterval interval : bridge.getIntervals()){
            int intervalState = interval.currentState();
            int possibleBridgeState;
            switch (intervalState){
                case (TimeInterval.MORE_THAN_HOUR) :
                    possibleBridgeState = Bridge.BRIDGE_CONNECTED;
                    break;
                case (TimeInterval.LESSER_THAN_HOUR) :
                    possibleBridgeState = Bridge.BRIDGE_SOON;
                    break;
                default:
                    possibleBridgeState = Bridge.BRIDGE_RAISED;
                    break;
            }
            if (possibleBridgeState>bridgeState) {
                bridgeState = possibleBridgeState;
            }
        }
        return bridgeState;
    }

    public Calendar getClosestStart(){
        Calendar closestStart = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        closestStart.add(Calendar.DATE,2);
        for (TimeInterval interval : bridge.getIntervals()){
            if ((closestStart.compareTo(interval.startCalendar)>0)&&(closestStart.compareTo(now)>0)){
                closestStart = ((Calendar) interval.startCalendar.clone());
            }
        }
        return closestStart;
    }

    public static Calendar getClosestStart(Bridge bridge){
        Calendar closestStart = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        closestStart.add(Calendar.DATE,2);
        for (TimeInterval interval : bridge.getIntervals()){
            if ((closestStart.compareTo(interval.startCalendar)>0)&&(closestStart.compareTo(now)>0)){
                closestStart = ((Calendar) interval.startCalendar.clone());
            }
        }
        return closestStart;
    }

    public static void makeBridgeBar(Bridge bridge, View bar){
        ((TextView) bar.findViewById(R.id.bridgeName)).setText(bridge.getName());
        //TODO: Замени дефис на тире
        String formattedTime = "";
        for (TimeInterval interval : bridge.getIntervals()) {
            formattedTime += interval + "\t";
        }
        switch (currentBridgeState(bridge)) {
            case (Bridge.BRIDGE_CONNECTED):
                ((ImageView) bar.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_normal);
                break;
            case (Bridge.BRIDGE_SOON):
                ((ImageView) bar.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_soon);
                break;
            case (Bridge.BRIDGE_RAISED):
                ((ImageView) bar.findViewById(R.id.image_bridgeState)).setImageResource(R.drawable.ic_bridge_late);
                break;
        }
        ((TextView) bar.findViewById(R.id.bridgeTime)).setText(formattedTime);
        //TODO:Разобраться с колокольчиком и уведомлением
        PendingIntent possibleNotification = PendingIntent.getBroadcast(bar.getContext(),bridge.getId(),new Intent(NotificationReceiver.CALL_NOTIFICATION),PendingIntent.FLAG_NO_CREATE);
        if (possibleNotification!=null){
            ((ImageView) bar.findViewById(R.id.image_isSubscribed)).setImageResource(R.drawable.ic_bell_on);
            Log.i("Bell","Notification found");
        } else {
            ((ImageView) bar.findViewById(R.id.image_isSubscribed)).setImageResource(R.drawable.ic_bell_off);
            Log.i("Bell","No notification found");
        }
    }

}
