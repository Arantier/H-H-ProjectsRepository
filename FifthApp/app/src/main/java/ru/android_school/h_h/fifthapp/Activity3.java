package ru.android_school.h_h.fifthapp;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class Activity3 extends AppCompatActivity {

    protected final static int CALL_TEXT = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_3);
        findViewById(R.id.button_goTo1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Activity3.this,Activity1.class));
            }
        });
        findViewById(R.id.button_goTo5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(Activity3.this,Activity5.class),CALL_TEXT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((data == null) || (resultCode != RESULT_OK)) {return;}
        if (requestCode==CALL_TEXT) {
            String name = data.getStringExtra("message");
            Snackbar.make(findViewById(R.id.activity3), name, Snackbar.LENGTH_SHORT).show();
        }
    }
}
