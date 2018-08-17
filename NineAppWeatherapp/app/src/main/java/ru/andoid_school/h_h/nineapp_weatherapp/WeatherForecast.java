package ru.andoid_school.h_h.nineapp_weatherapp;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;
import java.util.Calendar;

public class WeatherForecast {
    String weather,
            weatherDescription,
            locationName;
    double temperatureMin;
    double temperatureMed;
    double temperatureMax;
    double pressure;
    private double seaLevel;
    private double groundLevel;
    double windSpeed;
    double windDirectionDegrees;
    int humidity;
    Calendar sunrise, sunset;

    public static class JSONAdapter extends TypeAdapter<WeatherForecast> {

        @Override
        public void write(JsonWriter out, WeatherForecast value) {

        }

        @Override
        public WeatherForecast read(JsonReader in) throws IOException {
            WeatherForecast forecast = new WeatherForecast();
            in.beginObject();
            //Пипец вложенность конечно
            while (in.hasNext()) {
                switch (in.nextName()) {
                    case "weather":
                        in.beginArray();
                        while (in.hasNext()) {
                            in.beginObject();
                            while (in.hasNext()) {
                                switch (in.nextName()) {
                                    case "main":
                                        forecast.weather = in.nextString();
                                        break;
                                    case "description":
                                        forecast.weatherDescription = in.nextString();
                                        break;
                                    default:
                                        in.skipValue();
                                        break;
                                }
                            }
                            in.endObject();
                        }
                        in.endArray();
                        break;
                    case "main":
                        in.beginObject();
                        while (in.hasNext()) {
                            switch (in.nextName()) {
                                case "temp":
                                    forecast.temperatureMed = in.nextDouble();
                                    break;
                                case "pressure":
                                    forecast.pressure = in.nextDouble();
                                    break;
                                case "humidity":
                                    forecast.humidity = in.nextInt();
                                    break;
                                case "temp_min":
                                    forecast.temperatureMin = in.nextDouble();
                                    break;
                                case "temp_max":
                                    forecast.temperatureMax = in.nextDouble();
                                    break;
                                case "sea_level":
                                    forecast.seaLevel = in.nextDouble();
                                    break;
                                case "grnd_level":
                                    forecast.groundLevel = in.nextDouble();
                                    break;
                                default:
                                    in.skipValue();
                                    break;
                            }
                        }
                        in.endObject();
                        break;
                    case "wind" :
                        in.beginObject();
                        while (in.hasNext()) {
                            String name = in.nextName();
                            if (name.equals("speed")) {
                                forecast.windSpeed = in.nextDouble();
                            } else if (name.equals("deg")) {
                                forecast.windDirectionDegrees = in.nextDouble();
                            } else {
                                in.skipValue();
                            }
                        }
                        in.endObject();
                        break;
                    case "sys":
                        in.beginObject();
                        while (in.hasNext()) {
                            String name = in.nextName();
                            if (name.equals("sunrise")) {
                                forecast.sunrise = Calendar.getInstance();
                                forecast.sunrise.setTimeInMillis(in.nextLong()*1000);
                            } else if (name.equals("sunset")) {
                                forecast.sunset = Calendar.getInstance();
                                forecast.sunset.setTimeInMillis(in.nextLong()*1000);
                            } else {
                                in.skipValue();
                            }
                        }
                        in.endObject();
                        break;
                    case "name":
                        forecast.locationName = in.nextString();
                        break;
                    default:
                        in.skipValue();
                        break;
                }
            }
            in.endObject();
            return forecast;
        }
    }

    public WeatherForecast() {
    }

    @Override
    public String toString() {
        return "WeatherForecast{" +
                "weather='" + weather + '\'' +
                ", weatherDescription='" + weatherDescription + '\'' +
                ", locationName='" + locationName + '\'' +
                ", temperatureMin=" + temperatureMin +
                ", temperatureMed=" + temperatureMed +
                ", temperatureMax=" + temperatureMax +
                ", pressure=" + pressure +
                ", seaLevel=" + seaLevel +
                ", groundLevel=" + groundLevel +
                ", windSpeed=" + windSpeed +
                ", windDirectionDegrees=" + windDirectionDegrees +
                ", humidity=" + humidity +
                ", sunrise=" + sunrise +
                ", sunset=" + sunset +
                '}';
    }
}
