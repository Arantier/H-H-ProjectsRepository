package ru.andoid_school.h_h.tenapp_weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class UpdaterService extends Service {

    UpdaterServiceBinder binder = new UpdaterServiceBinder();
    OnUpdateForecastView forecastViewUpdater;

    public interface OnUpdateForecastView {
        public void onUpdateForecastView(WeatherForecast forecast);
    }

    public class UpdaterServiceBinder extends Binder {

        public UpdaterService getService() {
            return UpdaterService.this;
        }
    }

    public UpdaterService() {

    }

    public void getForecast() {
        Retrofit.Builder weatherBuilder = new Retrofit.Builder();
        Gson weatherGsonParser = new GsonBuilder()
                .registerTypeHierarchyAdapter(WeatherForecast.class, new WeatherForecast.JSONAdapter())
                .serializeNulls()
                .create();
        Retrofit retrofit = weatherBuilder.baseUrl("http://api.openweathermap.org/data/2.5/")
                .addConverterFactory(GsonConverterFactory.create(weatherGsonParser))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        retrofit.create(WeatherSiteApi.class).getWeatherForecast()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<WeatherForecast>() {
                    @Override
                    public void accept(WeatherForecast forecast) throws Exception {
                        forecastViewUpdater.onUpdateForecastView(forecast);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });
    }

    public void setUpdater(OnUpdateForecastView forecastViewUpdater) {
        this.forecastViewUpdater = forecastViewUpdater;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Flowable.interval(1, TimeUnit.MINUTES)
                .startWith((long) 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) throws Exception {
                        Log.i("ForecastQuerry","Запрос на погоду выполняется");
                        getForecast();
                    }
                });
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
