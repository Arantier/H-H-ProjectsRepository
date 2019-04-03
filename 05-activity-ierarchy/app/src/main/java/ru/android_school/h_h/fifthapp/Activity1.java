package ru.android_school.h_h.fifthapp;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity1 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_1);
        findViewById(R.id.button_goTo4).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(getTimeIntent(Activity1.this));
            }
        });
        findViewById(R.id.button_goTo2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity1.this,Activity2.class));
            }
        });
    }

    //Доработать
    public static Intent getTimeIntent(Context activityContext){
        long currentTime = System.currentTimeMillis();
        Intent intent = new Intent(activityContext,Activity4.class);
        intent.putExtra("current_time",currentTime);
        return intent;
    }
}
