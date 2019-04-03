package ru.andoid_school.h_h.nineapp_weatherapp;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class ForecastUpdaterService extends Service {

    public static final String LOG_TAG = "forecast_update";

    private final UpdaterServiceBinder binder = new UpdaterServiceBinder();
    private OnUpdateForecastView forecastViewUpdater;

    public interface OnUpdateForecastView {
        void onUpdateForecastView(WeatherForecast forecast);
    }

    public class UpdaterServiceBinder extends Binder {

        public ForecastUpdaterService getService() {
            return ForecastUpdaterService.this;
        }
    }

    public ForecastUpdaterService() {

    }

    private void getForecast() {
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
                    public void accept(WeatherForecast forecast) {
                        Log.i(LOG_TAG,"Запрос погоды выполнен успешно");
                        forecastViewUpdater.onUpdateForecastView(forecast);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        Log.e(LOG_TAG,"Запрос погоды провалился");
                        throwable.printStackTrace();
                    }
                });
    }

    public void setUpdater(OnUpdateForecastView forecastViewUpdater) {
        this.forecastViewUpdater = forecastViewUpdater;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Flowable.interval(1, TimeUnit.MINUTES)
                .startWith((long) 0)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Long>() {
                    @Override
                    public void accept(Long aLong) {
                        Log.i(LOG_TAG,"Запрос на погоду выполняется");
                        getForecast();
                    }
                });
        return binder;
    }

}
