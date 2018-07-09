package ru.androidschool.h_h.sixthapp;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    protected Fragment1 fragment1;
    protected Fragment2 fragment2;
    protected Fragment3 fragment3;

    class OnClickShowTitleToast implements MenuItem.OnMenuItemClickListener {

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {
            String title = menuItem.getTitle().toString();
            Toast.makeText(getApplicationContext(),title,Toast.LENGTH_SHORT).show();
            return true;
        }
    }

    protected void makeToolbar(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu_black);
        toolbar.setTitle(R.string.bottom_item1);
        toolbar.inflateMenu(R.menu.menu_toolbar);
        Menu toolbarMenu = toolbar.getMenu();
        toolbarMenu.findItem(R.id.toolbar_search).setOnMenuItemClickListener(new OnClickShowTitleToast());
        toolbarMenu.findItem(R.id.toolbar_menu_option1).setOnMenuItemClickListener(new OnClickShowTitleToast());
        toolbarMenu.findItem(R.id.toolbar_menu_option2).setOnMenuItemClickListener(new OnClickShowTitleToast());
        toolbarMenu.findItem(R.id.toolbar_menu_option3).setOnMenuItemClickListener(new OnClickShowTitleToast());
    }

    protected void makeNavigationView(){
        Toolbar toolbar = findViewById(R.id.toolbar);
        final DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navView = findViewById(R.id.navView);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
        navView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                Toast.makeText(getApplicationContext(),menuItem.getTitle(),Toast.LENGTH_SHORT).show();
                return true;
            }
        });
    }

    protected void makeBottom(){
        BottomNavigationView bottom = findViewById(R.id.bottom_navView);
        bottom.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                switch (menuItem.getItemId()) {
                    case (R.id.bottom_fragment1) :
                        //Choose fragment 1
                        if (manager.findFragmentByTag(Fragment1.TAG)==null){
                            ((Toolbar)findViewById(R.id.toolbar)).setTitle(R.string.bottom_item1);
                            transaction.replace(R.id.layout_fragmentContainer,fragment1,Fragment1.TAG);
                            break;
                        } else {
                            Log.i("fragments","Fragment1 already active");
                            return false;
                        }
                    case (R.id.bottom_fragment2) :
                        //Choose fragment 2
                        if (manager.findFragmentByTag(Fragment2.TAG)==null){
                            ((Toolbar)findViewById(R.id.toolbar)).setTitle(R.string.bottom_item2);
                            transaction.replace(R.id.layout_fragmentContainer,fragment2,Fragment2.TAG);
                            break;
                        } else {
                            Log.i("fragments", "Fragment2 already active");
                            return false;
                        }
                    case (R.id.bottom_fragment3) :
                        //Choose fragment 3
                        if (manager.findFragmentByTag(Fragment3.TAG)==null){
                            ((Toolbar)findViewById(R.id.toolbar)).setTitle(R.string.bottom_item3);
                            transaction.replace(R.id.layout_fragmentContainer,fragment3,Fragment3.TAG);
                            break;
                        } else {
                            Log.i("fragments","Fragment3 already active");
                            return false;
                        }
                    default:
                        return false;
                }
                transaction.commit();
                return true;
            }
        });
    }

    protected void attachFragments(){
        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layout_fragmentContainer,fragment1,Fragment1.TAG)
                .commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        makeToolbar();
        makeNavigationView();
        makeBottom();
        attachFragments();
    }
}
