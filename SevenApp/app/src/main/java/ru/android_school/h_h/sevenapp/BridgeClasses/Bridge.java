package ru.android_school.h_h.sevenapp.BridgeClasses;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.ArrayList;

@Entity
public class Bridge implements Parcelable {

    @PrimaryKey
    protected int id;

    //TODO:Переработать весь класс, добавить поддержку TimeIntervals для Room (а надо ли?)
    private String name;
    private String description;
    private ArrayList<TimeInterval> intervals;
    private String photoBridgeOpenURL;
    private String photoBridgeClosedURL;
    private double latitude;
    private double longitude;

    public static final String TAG = "bridge";

    public static final int BRIDGE_RAISED = 2;
    public static final int BRIDGE_SOON = 1;
    public static final int BRIDGE_CONNECTED = 0;

    //Parcelable methods and constructor
    //==============================================
    protected Bridge(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        intervals = in.readArrayList(TimeInterval.class.getClassLoader());
        photoBridgeOpenURL = in.readString();
        photoBridgeClosedURL = in.readString();
    }

    public static final Creator<Bridge> CREATOR = new Creator<Bridge>() {
        @Override
        public Bridge createFromParcel(Parcel in) {
            return new Bridge(in);
        }

        @Override
        public Bridge[] newArray(int size) {
            return new Bridge[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(getId());
        parcel.writeString(getName());
        parcel.writeString(getDescription());
        parcel.writeList(getIntervals());
        parcel.writeString(getPhotoBridgeOpenURL());
        parcel.writeString(getPhotoBridgeClosedURL());
    }

//==============================================

    //Getters
    //================================
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoBridgeOpenURL() {
        return photoBridgeOpenURL;
    }

    public String getPhotoBridgeClosedURL() {
        return photoBridgeClosedURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<TimeInterval> getIntervals() {
        return intervals;
    }

    //======================================
    //Setters

    //======================================
    public Bridge setId(int id) {
        this.id = id;
        return this;
    }

    public Bridge setName(String name) {
        this.name = name;
        return this;
    }

    public Bridge setDescription(String description) {
        this.description = description;
        return this;
    }

    public Bridge setPhotoBridgeOpenURL(String photoBridgeOpenURL) {
        this.photoBridgeOpenURL = photoBridgeOpenURL;
        return this;
    }

    public Bridge setPhotoBridgeClosedURL(String photoBridgeClosedURL) {
        this.photoBridgeClosedURL = photoBridgeClosedURL;
        return this;
    }

    public Bridge setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public Bridge setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    //======================================

    public Bridge() {
        intervals = new ArrayList<>();
    }

    public Bridge addInterval(TimeInterval interval) {
        intervals.add(interval);
        return this;
    }

    public Bridge addInterval(String start, String end) {
        try {
            intervals.add(new TimeInterval(start, end));
        } catch (TimeInterval.TimeIntervalInputException e) {
            Log.e(TAG, "Error: bad interval:\n" + e);
        }
        return this;
    }

    @Override
    public String toString() {
        String result = "";
        result += "id:" + id + "\n";
        result += "description:" + description + "\n";
        for (TimeInterval i : getIntervals()) {
            result += "\tInterval:" + i + "\n";
        }
        return result;
    }

    public static Bridge makeDebugBridge() {
        Bridge debug = new Bridge();
        debug.setName("Отладочный мост")
                .setDescription("Обычный отладочный мост")
                .setId(0)
                .setPhotoBridgeOpenURL("")
                .addInterval("0:00", "2:00");
        return debug;
    }
}