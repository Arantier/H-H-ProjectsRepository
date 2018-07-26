package ru.android_school.h_h.sevenapp.MainActivity;

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
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;
import ru.android_school.h_h.sevenapp.R;

public class MainActivity extends AppCompatActivity implements ErrorFragment.Refreshable{

    Toolbar toolbar;

    public static final String TAG = "MainActivity";

    @Override
    public void refresh() {

    }

    private class ListCallback implements Callback<ArrayList<Bridge>> {

        private ArrayList<Bridge> list;

        @Override
        public void onResponse(Call<ArrayList<Bridge>> call, Response<ArrayList<Bridge>> response) {
            list = response.body();
        }

        @Override
        public void onFailure(Call<ArrayList<Bridge>> call, Throwable t) {
            list = null;
        }

        public ArrayList<Bridge> getList() {
            return list;
        }
    }

    protected void blockMapButton(boolean isBlock){
        toolbar.getMenu()
                .getItem(0)
                .setEnabled(!isBlock);
    }

    //Не особо свичится
    protected void switchMapButton(){
        MenuItem menuItem =toolbar.getMenu()
                .getItem(0);
        menuItem.setChecked(!menuItem.isChecked());
    }

    protected void switchMapButton(boolean isList){
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
        Disposable disposable = Observable.create((ObservableOnSubscribe<ArrayList<Bridge>>) emitter -> {
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
            serverApi.receiveBridgesFlat()
                    .enqueue(new Callback<ArrayList<Bridge>>() {
                        @Override
                        public void onResponse(Call<ArrayList<Bridge>> call, Response<ArrayList<Bridge>> response) {
                            if (response.body()!=null) {
                                Log.i("rxJava","Responce correct");
                                emitter.onNext(response.body());
                                emitter.onComplete();
                            } else {
                                Log.i("rxJava","Responce incorrect");
                                emitter.onError(new Throwable("Error: responce is incorrect"));
                            }
                        }

                        @Override
                        public void onFailure(Call<ArrayList<Bridge>> call, Throwable t) {
                            Log.i("rxJava","Responce failed:"+t);
                            emitter.onError(t);
                        }
                    });
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(receivedList ->{
                    ListFragment list = ListFragment.newInstance(receivedList);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, list)
                            .commit();
                    blockMapButton(false);
                }, error ->{
                    ErrorFragment errFragment = ErrorFragment.newInstance();
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragmentContainer, errFragment)
                            .commit();
                    blockMapButton(true);
                });
    }
}
