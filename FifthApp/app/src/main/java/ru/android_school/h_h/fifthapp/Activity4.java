package ru.android_school.h_h.fifthapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Activity4 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_4);
        findViewById(R.id.button_cycle4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(Activity1.getTimeIntent(Activity4.this));
            }
        });
        onNewIntent(getIntent());
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);
        long receivedTime = intent.getLongExtra("current_time",0);
        TextView dateField = findViewById(R.id.textView_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.mm.yy hh:mm:ss");
        dateField.setText(dateFormat.format(new Date(receivedTime)));
    }
}
