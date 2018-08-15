package ru.andoid_school.h_h.tenapp_weatherapp;

import android.Manifest;
import android.app.IntentService;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

public class ImageDownloadService extends IntentService {

    public static final String LINK_INTENT_TAG = "link tag";

    public ImageDownloadService() {
        super("ImageDownloadService");
    }

    //TODO:Надо бы коллбек на прогресс прикрутить. Или может прямо из тела сделать
    void downloadFile(ResponseBody responseBody, File receivedArchive) throws Exception {
        InputStream receivingStream = null;
        OutputStream fileWritingStream = null;
        try {
            byte[] buffer = new byte[4096];
            long fileSize = responseBody.contentLength();
            long fileSizeDownloaded = 0;
            if (fileSize == -1) {
                //TODO:Вывести сообщение, что размер неизвестен
            }
            receivingStream = responseBody.byteStream();
            fileWritingStream = new FileOutputStream(receivedArchive);
            for (int readLength = receivingStream.read(buffer); readLength != -1; readLength = receivingStream.read(buffer)) {
                fileWritingStream.write(buffer, 0, readLength);
                fileSizeDownloaded += readLength;
                Log.i("ReceivingTheImage","Файл принимается, получено "+fileSizeDownloaded+" байт из "+fileSize);
            }
            fileWritingStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (receivingStream != null) {
                receivingStream.close();
            }
            if (fileWritingStream != null) {
                fileWritingStream.close();
            }
        }
    }

    File unzipArchive(File archivedFile, File readyFile) {
        FileInputStream fileInputStream;
        byte[] buffer = new byte[4096];
        try {
            Log.i("ReceivingTheImage", "Файл извлекается...");
            fileInputStream = new FileInputStream(archivedFile);
            ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
            //Буду надеяться, что в архиве и правда один элемент. Ну спецификация формата передачи данных такая, так что почему бы и нет?
            FileOutputStream fileOutputStream = new FileOutputStream(readyFile);
            if (zipInputStream.getNextEntry()==null) {
                Log.i("ReceivingTheImage","Архив пустой или неправильный");
            }
            for (int readLength = zipInputStream.read(buffer); readLength != -1; readLength = zipInputStream.read(buffer)) {
                fileOutputStream.write(buffer, 0, readLength);
            }
            fileOutputStream.close();
            zipInputStream.closeEntry();
            zipInputStream.close();
            fileInputStream.close();
        } catch (IOException e) {
            Log.e("ReceivingTheImage","Ошибка в разархивировывании файла!"+e);
        }
        return readyFile;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl("https://vk.com/")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(FileDownloadApi.class)
                .downloadFile(intent.getStringExtra(LINK_INTENT_TAG))
//                .downloadFileDebug()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<ResponseBody>() {
                    @Override
                    public void accept(ResponseBody responseBody) throws Exception {
                        File receivedArchive = new File(getFilesDir(), "downloadedArchive");
                        downloadFile(responseBody, receivedArchive);
                        Log.i("ReceivingTheImage", "Файл принят:"+receivedArchive.getAbsolutePath()
                                +", размер файла (в б): "+receivedArchive.length());
                        File readyImage = new File(getFilesDir(), MainActivity.fileName);
                        readyImage = unzipArchive(receivedArchive, readyImage);
                        Log.i("ReceivingTheImage", "Файл разархивирован:"+readyImage.getAbsolutePath()
                                +", размер файла (в б): "+readyImage.length());
                        receivedArchive.delete();
                        Log.i("ReceivingTheImage", "Архив удалён. Сервис выключается");
                        ImageDownloadService.this.stopSelf();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        throwable.printStackTrace();
                    }
                });

    }
}
