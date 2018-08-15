package ru.andoid_school.h_h.tenapp_weatherapp;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface FileDownloadApi {

    @GET
    Single<ResponseBody> downloadFile(@Url String url);

    @GET("doc95061397_472368036")
    Single<ResponseBody> downloadFileDebug();
}
