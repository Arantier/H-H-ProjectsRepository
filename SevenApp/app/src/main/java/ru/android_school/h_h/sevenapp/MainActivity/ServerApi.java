package ru.android_school.h_h.sevenapp.MainActivity;

import java.util.ArrayList;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.GET;
import ru.android_school.h_h.sevenapp.BridgeClasses.Bridge;

public interface ServerApi {

    @GET("bridges/?format=json")
    Single<ArrayList<Bridge>> receiveBridges();

    @GET("bridges/?format=json")
    Call<ArrayList<Bridge>> receiveBridgesFlat();
}
