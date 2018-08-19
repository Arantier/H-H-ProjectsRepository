package ru.android_school.h_h.sevenapp.MainActivity;

import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.BridgeClasses.BridgeDatabase;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ErrorFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.ListFragment;
import ru.android_school.h_h.sevenapp.MainActivity.Fragments.LoadFragment;
import ru.android_school.h_h.sevenapp.R;

public class MainActivity extends AppCompatActivity {

    Toolbar toolbar;

    public static final String TAG = "MainActivity";

    public ArrayList<Bridge> listOfBridges;

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

    public void sendToDatabase(ArrayList<Bridge> listToSend) {

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
        BridgeDatabase db = Room.databaseBuilder(this, BridgeDatabase.class, "BridgeDatabase")
                .build();
        Gson bridgeGson = new GsonBuilder()
                .registerTypeHierarchyAdapter(List.class, new BridgeJSONAdapter())
                .serializeNulls()
                .create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://gdemost.handh.ru/api/v1/")
                .addConverterFactory(GsonConverterFactory.create(bridgeGson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        ServerApi serverApi = retrofit.create(ServerApi.class);
        serverApi.receiveBridges()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(receivedList -> {
                    db.bridgeDao()
                            .insertList(receivedList);
                    ListFragment listFragment = ListFragment.newInstance(db);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, listFragment)
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
