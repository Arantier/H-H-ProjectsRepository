package ru.andoid_school.h_h.nineapp_weatherapp;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.zip.ZipInputStream;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;

public class ImageDownloadService extends IntentService {

    public static final String LINK_INTENT_TAG = "link tag";
    public static final String IMAGE_DOWNLOAD_TAG = "img_download_tag";

    private String url;

    public ImageDownloadService() {
        super("ImageDownloadService");
    }

    void downloadFile(ResponseBody responseBody, File receivedArchive) throws IOException {
        InputStream receivingStream = null;
        OutputStream fileWritingStream = null;
        try {
            byte[] buffer = new byte[4096];
            long fileSize = responseBody.contentLength();
            Intent downloadStartIntent = new Intent("ru.android_school.h_h.DOWNLOAD_START");
            Log.i( IMAGE_DOWNLOAD_TAG, "Загрузка началась, размер:" + fileSize);
            downloadStartIntent.putExtra(ImageDownloadStatusReceiver.FILE_SIZE, fileSize);
            sendBroadcast(downloadStartIntent);
            long fileSizeDownloaded = 0;
            receivingStream = responseBody.byteStream();
            fileWritingStream = new FileOutputStream(receivedArchive);
            for (int readLength = receivingStream.read(buffer); readLength != -1; readLength = receivingStream.read(buffer)) {
                fileWritingStream.write(buffer, 0, readLength);
                fileSizeDownloaded += readLength;
                Intent fileDownloadUpdate = new Intent("ru.android_school.h_h.DOWNLOAD_PROGRESS");
                Log.i(IMAGE_DOWNLOAD_TAG, "Загрузка, принято " + fileSizeDownloaded + " из " + fileSize);
                fileDownloadUpdate.putExtra(ImageDownloadStatusReceiver.DOWNLOADED_SIZE, fileSizeDownloaded);
                sendBroadcast(fileDownloadUpdate);
            }
            fileWritingStream.flush();
            Intent fileDownloadFinished = new Intent("ru.android_school.h_h.DOWNLOAD_FINISHED");

            Log.i(IMAGE_DOWNLOAD_TAG, "Файл загружен, распаковка...");
            sendBroadcast(fileDownloadFinished);
        } finally {
            if (receivingStream != null) {
                receivingStream.close();
            }
            if (fileWritingStream != null) {
                fileWritingStream.close();
            }
        }
    }

    private void downloadFile(String url, File receivedArchive) throws IOException {
        URL fileUrl = new URL(url);
        Log.i(IMAGE_DOWNLOAD_TAG, "Файл принимается по URL:" + url);
        InputStream receivingStream = null;
        OutputStream fileWritingStream = null;
        try {
            HttpURLConnection urlConnection = (HttpURLConnection) fileUrl.openConnection();
            urlConnection.connect();
            Intent downloadStartIntent = new Intent("ru.android_school.h_h.DOWNLOAD_START");

            long fileSize = urlConnection.getContentLength();
            Log.i( IMAGE_DOWNLOAD_TAG, "Загрузка началась, размер:" + fileSize);
            downloadStartIntent.putExtra(ImageDownloadStatusReceiver.FILE_SIZE, fileSize);
            sendBroadcast(downloadStartIntent);

            receivingStream = urlConnection.getInputStream();
            fileWritingStream = new FileOutputStream(receivedArchive);
            byte[] buffer = new byte[4096];
            long fileSizeDownloaded = 0;
            for (int readLength = receivingStream.read(buffer); readLength != -1; readLength = receivingStream.read(buffer)) {
                fileWritingStream.write(buffer, 0, readLength);
                fileSizeDownloaded += readLength;
                Intent fileDownloadUpdate = new Intent("ru.android_school.h_h.DOWNLOAD_PROGRESS");
                Log.i(IMAGE_DOWNLOAD_TAG, "Загрузка, принято " + fileSizeDownloaded + " из " + fileSize);
                fileDownloadUpdate.putExtra(ImageDownloadStatusReceiver.DOWNLOADED_SIZE, fileSizeDownloaded);
                sendBroadcast(fileDownloadUpdate);
            }
            fileWritingStream.flush();
            urlConnection.disconnect();
            Intent fileDownloadFinished = new Intent("ru.android_school.h_h.DOWNLOAD_FINISHED");
            Log.i(IMAGE_DOWNLOAD_TAG, "Файл загружен, распаковка...");
            sendBroadcast(fileDownloadFinished);
        } finally {
            if (receivingStream != null) {
                receivingStream.close();
            }
            if (fileWritingStream != null) {
                fileWritingStream.close();
            }
        }
    }

    private void unzipArchive(File archivedFile, File readyFile) throws IOException {
        Log.i(IMAGE_DOWNLOAD_TAG,"Распаковка архива...");
        FileInputStream fileInputStream;
        byte[] buffer = new byte[4096];
        fileInputStream = new FileInputStream(archivedFile);
        ZipInputStream zipInputStream = new ZipInputStream(fileInputStream);
        //Буду надеяться, что в архиве и правда один элемент. Ну спецификация формата передачи данных такая, так что почему бы и нет?
        FileOutputStream fileOutputStream = new FileOutputStream(readyFile);
        if (zipInputStream.getNextEntry() == null) {
            throw new IOException("Archive is empty");
        }
        for (int readLength = zipInputStream.read(buffer); readLength != -1; readLength = zipInputStream.read(buffer)) {
            fileOutputStream.write(buffer, 0, readLength);
        }
        fileOutputStream.close();
        zipInputStream.closeEntry();
        zipInputStream.close();
        fileInputStream.close();
        Log.i(IMAGE_DOWNLOAD_TAG,"Файл распакован!");
        Intent unzipFinished = new Intent("ru.android_school.h_h.UNZIPPING_FINISHED");
        sendBroadcast(unzipFinished);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //////////////////////////////////////////////////////////
        //  Старый код с ретрофитом, не поддерживает прогресс   //
        //////////////////////////////////////////////////////////
//        Retrofit.Builder builder = new Retrofit.Builder();
//        builder.baseUrl("https://vk.com/")
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .build()
//                .create(FileDownloadApi.class)
//                .downloadFile(intent.getStringExtra(LINK_INTENT_TAG))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<ResponseBody>() {
//                    @Override
//                    public void accept(ResponseBody responseBody) throws Exception {
//                        try {
//                            File receivedArchive = new File(getFilesDir(), "downloadedArchive");
//                            downloadFile(responseBody, receivedArchive);
//                            File readyImage = new File(getFilesDir(), MainActivity.fileName);
//                            unzipArchive(receivedArchive, readyImage);
//                            receivedArchive.delete();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            sendBroadcast(new Intent("ru.andoid_school.h_h.DOWNLOAD_FAILED"));
//                        }
//                        ImageDownloadService.this.stopSelf();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        sendBroadcast(new Intent("ru.andoid_school.h_h.DOWNLOAD_FAILED"));
//                        throwable.printStackTrace();
//                    }
//                });
        url = intent.getStringExtra(LINK_INTENT_TAG);
        Single.create(new SingleOnSubscribe<File>() {
            @Override
            public void subscribe(SingleEmitter<File> emitter) {
                try {
                    File receivedArchive = new File(getFilesDir(), "downloadedArchive");
                    downloadFile(url, receivedArchive);
                    emitter.onSuccess(receivedArchive);
                } catch (IOException e) {
                    emitter.onError(e);
                }
            }
        })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(new Consumer<File>() {
                    @Override
                    public void accept(File receivedArchive) throws Exception {
                        File readyImage = new File(getFilesDir(), MainActivity.fileName);
                        unzipArchive(receivedArchive, readyImage);
                        receivedArchive.delete();
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) {
                        sendBroadcast(new Intent("ru.android_school.h_h.DOWNLOAD_FAILED"));
                        throwable.printStackTrace();
                    }
                });
        //////////////////////////////////////////////////////////////////////////
        //  Чёт не работает                                                     //
        //  TODO:Игорь, объясни почему пж, хочется всё таки понять реактивку    //
        //////////////////////////////////////////////////////////////////////////
//        Single.just(intent.getStringExtra(LINK_INTENT_TAG))
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeOn(Schedulers.io())
//                .subscribe(new Consumer<String>() {
//                    @Override
//                    public void accept(String url) throws Exception {
//                        try {
//                            File receivedArchive = new File(getFilesDir(), "downloadedArchive");
//                            downloadFile(url, receivedArchive);
//                            File readyImage = new File(getFilesDir(), MainActivity.fileName);
//                            unzipArchive(receivedArchive, readyImage);
//                            receivedArchive.delete();
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            sendBroadcast(new Intent("ru.andoid_school.h_h.DOWNLOAD_FAILED"));
//                        }
//                        ImageDownloadService.this.stopSelf();
//                    }
//                }, new Consumer<Throwable>() {
//                    @Override
//                    public void accept(Throwable throwable) throws Exception {
//                        sendBroadcast(new Intent("ru.andoid_school.h_h.DOWNLOAD_FAILED"));
//                        throwable.printStackTrace();
//                    }
//                });
    }
}
