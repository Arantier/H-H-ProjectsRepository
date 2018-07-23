package ru.android_school.h_h.sevenapp;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import ru.android_school.h_h.sevenapp.support_classes.Bridge;

public interface ServerApi {

    @GET("bridges/?format=json")
    Single<ArrayList<Bridge>> receiveBridges();

    @GET("bridges/?format=json")
    Call<ArrayList<Bridge>> receiveBridgesFlat();
}
