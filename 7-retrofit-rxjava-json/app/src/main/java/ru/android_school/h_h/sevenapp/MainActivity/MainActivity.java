package ru.android_school.h_h.sevenapp.MainActivity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgePage.NotificationReceiver;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ErrorFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ListFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.LoadFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.MapFragment;
import ru.android_school.h_h.sevenapp.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    public static final String TAG = "MainActivity";
    public static final String LAUNCH_WITH_BRIDGE = "launch_bridge";
    public static final int PERMISSION_REQUEST_CODE = 123;

    public boolean isList;

    ArrayList<Bridge> listOfBridges;

    ListFragment listFragment;
    MapFragment mapFragment;

    protected void blockToolbarIcon(boolean isBlock) {
        toolbar.getMenu()
                .getItem(0)
                .setEnabled(!isBlock);
    }

    public void setList() {
        MenuItem menuItem = toolbar.getMenu()
                .getItem(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, listFragment)
                .commit();
        menuItem.setIcon(R.drawable.selector_toolbar_map);
        isList = true;
    }

    public void setMap() {
        MenuItem menuItem = toolbar.getMenu()
                .getItem(0);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, mapFragment)
                .commit();
        menuItem.setIcon(R.drawable.selector_toolbar_list);
        isList = false;
    }

    //Не особо свичится
    protected void switchModes() {
        if (isList) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission_group.LOCATION) == PackageManager.PERMISSION_GRANTED) {
                setMap();
            } else {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission_group.LOCATION)) {
                    AlertDialog permissionRequest = (new AlertDialog.Builder(this)).setTitle(R.string.permission_rationale_title)
                            .setMessage(R.string.permission_rationale_message)
                            .setPositiveButton(R.string.permission_rationale_close, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                }
                            }).create();
                    permissionRequest.getButton(AlertDialog.BUTTON_POSITIVE)
                            .setEnabled(false);
                    Single.timer(3, TimeUnit.SECONDS)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribeOn(Schedulers.io())
                            .subscribe(new Consumer<Long>() {
                                @Override
                                public void accept(Long aLong) throws Exception {
                                    permissionRequest.getButton(AlertDialog.BUTTON_POSITIVE)
                                            .setEnabled(true);
                                }
                            });
                    permissionRequest.show();
                } else {
                    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission_group.LOCATION}, PERMISSION_REQUEST_CODE);
                }
            }
        } else {
            setList();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_switch);
        toolbar.getMenu().findItem(R.id.app_bar_switch)
                .setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switchModes();
                        return true;
                    }
                });
        blockToolbarIcon(true);
        LoadFragment load = LoadFragment.newInstance();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentContainer, load)
                .commit();
        NotificationReceiver notificationReceiver = new NotificationReceiver();
        IntentFilter intentFilter = new IntentFilter(NotificationReceiver.MAKE_NOTIFICATION);
        intentFilter.addAction(NotificationReceiver.CALL_NOTIFICATION);
        intentFilter.addAction(NotificationReceiver.REMOVE_NOTIFICATION);
        this.registerReceiver(notificationReceiver, intentFilter);
        Gson bridgeGson = new GsonBuilder()
                .registerTypeHierarchyAdapter(List.class, new BridgeJSONAdapter())
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gdemost.handh.ru/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(bridgeGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(ServerApi.class)
                .receiveBridges()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(receivedList -> {
                    mapFragment = MapFragment.newInstance(receivedList);
                    listFragment = ListFragment.newInstance(receivedList);
                    setList();
                    blockToolbarIcon(false);
                }, error -> {
                    ErrorFragment errFragment = ErrorFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, errFragment)
                            .commit();
                    blockToolbarIcon(true);
                });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Toast.makeText(this, "разрешения приняты, кол-во:"+permissions.length, Toast.LENGTH_SHORT)
                .show();
        switch (requestCode) {
            case (PERMISSION_REQUEST_CODE):
                if ((grantResults.length != 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    setMap();
                } else {
                    Toast.makeText(this, "Доступ запрещён", Toast.LENGTH_SHORT)
                            .show();
                }
        }
    }
}
