package ru.android_school.h_h.sevenapp.MainActivity;

import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgePage.NotificationReceiver;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ErrorFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ListFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.LoadFragment;
import ru.android_school.h_h.sevenapp.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    public static final String TAG = "MainActivity";
    public static final String LAUNCH_WITH_BRIDGE = "launch_bridge";

    protected void blockMapButton(boolean isBlock) {
        toolbar.getMenu()
                .getItem(0)
                .setEnabled(!isBlock);
    }

    //Не особо свичится
    protected void switchMapButton() {
        MenuItem menuItem = toolbar.getMenu()
                .getItem(0);
        menuItem.setChecked(!menuItem.isChecked());
    }

    protected void switchMapButton(boolean isList) {
        toolbar.getMenu()
                .getItem(0)
                .setChecked(isList);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_switch);
        Log.i(TAG, "Map button was pressed");
        toolbar.setNavigationOnClickListener(view -> {
            switchMapButton(false);
        });
        blockMapButton(true);
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
                    ListFragment list = ListFragment.newInstance(receivedList);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, list)
                            .commit();
                    blockMapButton(false);
                }, error -> {
                    ErrorFragment errFragment = ErrorFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, errFragment)
                            .commit();
                    blockMapButton(true);
                });
    }
}
