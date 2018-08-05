package ru.androidschool.h_h.eightapp;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public static final String LOG_TAG = "MainActivityLogTag";

    Toolbar toolbar;
    RecyclerView recyclerView;
    FloatingActionButton fab;

    ArrayList<Note> listOfNotes;

    private ArrayList<Note> searchItems(String stringToFind) {
        return listOfNotes;
    }

    private void setToolbar() {
        toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.inflateMenu(R.menu.menu_main_toolbar);
        MenuItem searchMenuItem = toolbar.getMenu().findItem(R.id.search);
        SearchView searchView = (SearchView) searchMenuItem.getActionView();
        searchMenuItem.setOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem menuItem) {
                Log.i(LOG_TAG, "Поиск развёрнут");
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem menuItem) {
                Log.i(LOG_TAG, "Поиск скрыт");
                return true;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.i(LOG_TAG, "Ищется строка \"" + s + "\"");
                searchItems(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(listener -> {
            startActivityForResult(new Intent(MainActivity.this,EditActivity.class),EditActivity.REQUEST_CODE);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==EditActivity.REQUEST_CODE){
            if (resultCode==AppCompatActivity.RESULT_OK){
                try {
                    listOfNotes.add(data.getParcelableExtra(EditActivity.INTENT_TAG));
                    Log.i("listOfNotes","New note added:"+listOfNotes.get(listOfNotes.size()-1));
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
