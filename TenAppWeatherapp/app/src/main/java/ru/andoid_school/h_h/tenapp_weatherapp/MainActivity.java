package ru.andoid_school.h_h.tenapp_weatherapp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.drawable.Drawable;
import android.os.IBinder;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements UpdaterService.OnUpdateForecastView {

    UpdaterService updaterService;
    boolean isServiceBound;
    static String fileName = "image.jpg";

    TextView weather,
            weatherDescription,
            weatherLocation,
            temperatureMin,
            temperatureMed,
            temperatureMax,
            pressure,
            windSpeed,
            humidity,
            sunrise,
            sunset,
            windDirection,
            downloadProgressStatus;

    ProgressBar downloadProgressBar;

    ImageView pictureHolder;

    private ServiceConnection updaterServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            updaterService = ((UpdaterService.UpdaterServiceBinder) iBinder).getService();
            updaterService.setUpdater(MainActivity.this);
            isServiceBound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            isServiceBound = false;
        }
    };

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.inflateMenu(R.menu.toolbar_menu);
        toolbar.getMenu().findItem(R.id.imageCallButton).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                Intent intentWithLink = new Intent(MainActivity.this, ImageDownloadService.class);
                intentWithLink.putExtra(ImageDownloadService.LINK_INTENT_TAG, "https://drive.google.com/uc?export=download&id=1fPAla77UWq7ITrI4gHtHCCD8I9qfQiTZ");
                startService(intentWithLink);
                return true;
            }
        });
    }

    public void setFileImageToView() {
        File imageFile = new File(getFilesDir(),fileName);
        if (imageFile.exists()){
            Drawable receivedImageDrawable = Drawable.createFromPath(imageFile.getAbsolutePath());
            pictureHolder.setImageDrawable(receivedImageDrawable);
        }
    }

    public void changeProgressStatus(){

    }

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
    }

    @Override
    protected void onStart() {
        super.onStart();
        bindService(new Intent(this, UpdaterService.class), updaterServiceConnection, Context.BIND_AUTO_CREATE);
        File imageFile = new File(getFilesDir(), fileName);
        if (imageFile.exists()) {
            setFileImageToView();
        } else {
            downloadProgressStatus.setText("Файл не загружен");
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (isServiceBound) {
            updaterService.setUpdater(null);
            unbindService(updaterServiceConnection);
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
}
