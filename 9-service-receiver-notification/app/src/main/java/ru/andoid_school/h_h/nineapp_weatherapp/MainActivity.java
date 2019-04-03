package ru.andoid_school.h_h.nineapp_weatherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.File;
import java.text.SimpleDateFormat;

public class MainActivity extends AppCompatActivity implements ForecastUpdaterService.OnUpdateForecastView, ImageDownloadStatusReceiver.StatusListener {

    private ForecastUpdaterService updaterService;
    private boolean isServiceBound;
    static final String fileName = "image.jpg";

    private TextView weather;
    private TextView weatherDescription;
    private TextView weatherLocation;
    private TextView temperatureMin;
    private TextView temperatureMed;
    private TextView temperatureMax;
    private TextView pressure;
    private TextView windSpeed;
    private TextView humidity;
    private TextView sunrise;
    private TextView sunset;
    private TextView windDirection;
    private TextView downloadProgressStatus;

    private ProgressBar downloadProgressBar;

    private ImageView pictureHolder;

    //////////////////////////////////////////////////
    //  Реализация интерфейса коллбека для ресивера //
    //////////////////////////////////////////////////

    @Override
    public void setSize(long size) {
        downloadProgressBar.setMax((int) size);
        Log.i("InterfaceReceive", "Файл начинает загрузку, размер:" + size);
        if (size > 0) {
            downloadProgressStatus.setText(String.format("Файл загружается, загружено %d из %d байт", downloadProgressBar.getProgress(), downloadProgressBar.getMax()));
        } else {
            downloadProgressStatus.setText("Размер файла неизвестен");
            downloadProgressBar.setIndeterminate(true);
        }
    }

    @Override
    public void setDownloadProgress(long downloadedSize) {
        Log.i("InterfaceReceive", "Файл загружается, размер:" + downloadedSize);
        if (downloadProgressBar.getMax() > 0) {
            Log.i("InterfaceReceive", " из " + downloadProgressBar.getMax());
            downloadProgressBar.setProgress((int) downloadedSize);
            downloadProgressStatus.setText(String.format("Файл загружается, загружено %d из %d байт", downloadProgressBar.getProgress(), downloadProgressBar.getMax()));
        } else {
            downloadProgressStatus.setText(String.format("Файл загружается, загружено %d байт", downloadedSize));
        }
    }

    @Override
    public void finishDownload() {
        downloadProgressBar.setIndeterminate(true);
        downloadProgressStatus.setText("Файл загружен, приступаю к распаковке");
    }

    @Override
    public void prepareImage() {
        downloadProgressBar.setIndeterminate(false);
        downloadProgressStatus.setText("Изображение готово!");
        setFileImageToView();
    }

    @Override
    public void downloadFailed() {
        downloadProgressBar.setIndeterminate(false);
        downloadProgressStatus.setText("Ошибка загрузки!");
    }

    //////////////////////////////////
    //  Конец интерфейса коллбека   //
    //////////////////////////////////

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.getMenu().findItem(R.id.imageCallButton).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intentWithLink = new Intent(MainActivity.this, ImageDownloadService.class);
                //Пожирнее, даже размер не определяется
//                intentWithLink.putExtra(ImageDownloadService.LINK_INTENT_TAG, "https://drive.google.com/uc?authuser=0&id=1bBSWGTjFESIACCeK3svSU2TDhdUOBegh");
                //Полегче, слишком быстро чтобы заметить
                intentWithLink.putExtra(ImageDownloadService.LINK_INTENT_TAG, "https://drive.google.com/uc?authuser=0&id=1FytRPfPBHtP33GhphGKF8d6uvyse9Po_");
                downloadProgressStatus.setText("Начинается загрузка...");
                downloadProgressBar.setIndeterminate(true);
                startService(intentWithLink);
                return true;
            }
        });
        toolbar.getMenu().findItem(R.id.imageDeleteButton).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                File imageFile = new File(getFilesDir(), fileName);
                imageFile.delete();
                setFileImageToView();
                return true;
            }
        });
    }

    private void setFileImageToView() {
        File imageFile = new File(getFilesDir(), fileName);
        if (imageFile.exists()) {
            Drawable receivedImageDrawable = Drawable.createFromPath(imageFile.getAbsolutePath());
            pictureHolder.setImageDrawable(receivedImageDrawable);
        } else {
            pictureHolder.setImageResource(0);
            downloadProgressStatus.setText("Файл не загружен");
        }
    }

    @Override
    public void onUpdateForecastView(WeatherForecast forecast) {
        Log.i("ForecastQuerry", "Запрос выполнен");
        weather.setText(forecast.weather);
        weatherDescription.setText(forecast.weatherDescription);
        weatherLocation.setText(forecast.locationName);
        temperatureMin.setText(String.format("Минимальная температура: %.2f °C", forecast.temperatureMin));
        temperatureMed.setText(String.format("Средняя температура: %.2f °C", forecast.temperatureMed));
        temperatureMax.setText(String.format("Максимальная температура: %.2f °C", forecast.temperatureMax));
        pressure.setText(String.format("Давление: %.2f мм рт. ст", forecast.pressure));
        windSpeed.setText(String.format("Сила ветра: %.2f м/с", forecast.windSpeed));
        humidity.setText(String.format("Влажность: %d%%", forecast.humidity));
        SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm");
        sunrise.setText("Восход:" + dateFormat.format(forecast.sunrise.getTime()));
        sunset.setText("Закат:" + dateFormat.format(forecast.sunset.getTime()));
        //SMOrc
        windDirection.setText(String.format("Ветер дует туда: %.2f°", forecast.windDirectionDegrees));
    }

    private final ServiceConnection updaterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updaterService = ((ForecastUpdaterService.UpdaterServiceBinder) iBinder).getService();
            updaterService.setUpdater(MainActivity.this);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setToolbar();
        weather = findViewById(R.id.weather);
        weatherDescription = findViewById(R.id.weatherDescription);
        weatherLocation = findViewById(R.id.weatherLocation);
        temperatureMin = findViewById(R.id.temperatureMin);
        temperatureMed = findViewById(R.id.temperatureMed);
        temperatureMax = findViewById(R.id.temperatureMax);
        pressure = findViewById(R.id.pressure);
        windSpeed = findViewById(R.id.windSpeed);
        humidity = findViewById(R.id.humidity);
        sunrise = findViewById(R.id.sunrise);
        sunset = findViewById(R.id.sunset);
        windDirection = findViewById(R.id.windDirection);
        downloadProgressBar = findViewById(R.id.downloadProgressBar);
        downloadProgressStatus = findViewById(R.id.downloadProgressStatus);
        pictureHolder = findViewById(R.id.imageView);
        ImageDownloadStatusReceiver receiver = new ImageDownloadStatusReceiver(this);
        IntentFilter filter = new IntentFilter("ru.android_school.h_h.DOWNLOAD_START");
        filter.addAction("ru.android_school.h_h.DOWNLOAD_PROGRESS");
        filter.addAction("ru.android_school.h_h.DOWNLOAD_FINISHED");
        filter.addAction("ru.android_school.h_h.UNZIPPING_FINISHED");
        filter.addAction("ru.android_school.h_h.DOWNLOAD_FAILED");
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, ForecastUpdaterService.class), updaterServiceConnection, Context.BIND_AUTO_CREATE);
        setFileImageToView();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceBound) {
            updaterService.setUpdater(null);
            unbindService(updaterServiceConnection);
        }
    }
}
