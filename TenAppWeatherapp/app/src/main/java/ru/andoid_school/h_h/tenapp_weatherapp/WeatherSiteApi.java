package ru.andoid_school.h_h.tenapp_weatherapp;

import io.reactivex.Flowable;
import io.reactivex.Single;
import retrofit2.http.GET;

public interface WeatherSiteApi {

    @GET("weather?q=saransk&units=metric&appid=a924f0f5b30839d1ecb95f0a6416a0c2")
    Single<WeatherForecast> getWeatherForecast();

}
