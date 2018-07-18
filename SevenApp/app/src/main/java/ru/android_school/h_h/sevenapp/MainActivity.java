package ru.android_school.h_h.sevenapp;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import io.reactivex.Flowable;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;
    ListFragment list;
    MapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_switch);
        toolbar.setNavigationOnClickListener(view -> {
            Log.i("navb", "Button was pressed");
        });
        list = ListFragment.newInstance();
//        map = MapFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer,list)
                .commit();
    }
}
