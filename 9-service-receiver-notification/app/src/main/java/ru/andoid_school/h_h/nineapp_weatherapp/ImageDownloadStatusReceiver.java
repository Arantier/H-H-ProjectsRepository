package ru.andoid_school.h_h.nineapp_weatherapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class ImageDownloadStatusReceiver extends BroadcastReceiver {

    public static final String FILE_SIZE = "file_size";
    public static final String DOWNLOADED_SIZE = "downloaded_size";

    interface StatusListener {
        void setSize(long size);

        void setDownloadProgress(long sizeOfReceived);

        void finishDownload();

        void prepareImage();

        void downloadFailed();
    }

    private StatusListener statusListener;

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        switch (action) {
            case "ru.android_school.h_h.DOWNLOAD_START":
                statusListener.setSize(intent.getLongExtra(FILE_SIZE, -1));
                statusListener.setDownloadProgress(0);
                break;
            case "ru.android_school.h_h.DOWNLOAD_PROGRESS":
                statusListener.setDownloadProgress(intent.getLongExtra(DOWNLOADED_SIZE, 0));
                break;
            case "ru.android_school.h_h.DOWNLOAD_FINISHED":
                statusListener.finishDownload();
                break;
            case "ru.android_school.h_h.DOWNLOAD_FAILED":
                statusListener.downloadFailed();
                break;
            case "ru.android_school.h_h.UNZIPPING_FINISHED":
                statusListener.prepareImage();
                break;
            default:
                break;
        }
    }

    public ImageDownloadStatusReceiver(StatusListener statusListener) {
        this.statusListener = statusListener;
    }
}
