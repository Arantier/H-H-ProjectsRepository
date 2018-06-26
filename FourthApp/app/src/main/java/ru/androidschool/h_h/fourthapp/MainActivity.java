package ru.androidschool.h_h.fourthapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

public class MainActivity extends AppCompatActivity {

    private RecyclerView RecyclerView;
    private RecyclerView.Adapter Adapter;
    private RecyclerView.LayoutManager LayoutManager;

    class Adapter extends Recy

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.current_screen);
        toolbar.setNavigationIcon(R.drawable.ic_arrow);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        RecyclerView = findViewById(R.id.recycler_view);
        RecyclerView.setLayoutManager(new GridLayoutManager(this,2));
    }
}
