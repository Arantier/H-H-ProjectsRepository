package com.example.dmitry.testingnotifications;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {

    public void createNotification(String s){
        Intent intent = new Intent(MyReceiver.SEND_NOTIFICATION_STRING);
        intent.putExtra(MyReceiver.STRING_EXTRA,s);
        sendBroadcast(intent);
    }

    public void createNotification(Parcelable p){
        Intent intent = new Intent(MyReceiver.SEND_NOTIFICATION_PARCELABLE);
        intent.putExtra(MyReceiver.PARCELABLE_EXTRA,p);
        sendBroadcast(intent);
    }

    public void createDelayedNotification(String s){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MyReceiver.SEND_NOTIFICATION_STRING);
        intent.putExtra(MyReceiver.STRING_EXTRA,s);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC, Calendar.getInstance().getTimeInMillis()+5000,pendingIntent);
    }

    public void createDelayedNotification(Parcelable p){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MyReceiver.SEND_NOTIFICATION_PARCELABLE);
        intent.putExtra(MyReceiver.PARCELABLE_EXTRA,p);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.set(AlarmManager.RTC,10000,pendingIntent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = findViewById(R.id.button);
        IntentFilter filter = new IntentFilter(MyReceiver.SEND_NOTIFICATION_STRING);
        filter.addAction(MyReceiver.SEND_NOTIFICATION_PARCELABLE);
        MyReceiver receiver = new MyReceiver();
        registerReceiver(receiver,filter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String stringToSend = "Seems it's working for instant";
                ParcelableClass pc = new ParcelableClass("Parcel test","Seems it also work for parcels");
                createNotification(stringToSend);
                createNotification(pc);
                stringToSend = "Seems it's working for delayed";
                pc.text+=", even for delayed ones";
                createDelayedNotification(stringToSend);
                createDelayedNotification(pc);
            }
        });
    }
}
