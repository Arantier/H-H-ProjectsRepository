package ru.android_school.h_h.sevenapp;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

class Bridge implements Parcelable {
    public int id;
    public String name;
    public String description;
    public ArrayList<TimeInterval> bridgeIntervals;
    public String photoBridgeOpenURL;
    public String photoBridgeClosedURL;
    public boolean isSubscribed;

    protected Bridge(Parcel in) {
        id = in.readInt();
        name = in.readString();
        description = in.readString();
        bridgeIntervals = in.readArrayList(TimeInterval.class.getClassLoader());
        photoBridgeOpenURL = in.readString();
        photoBridgeClosedURL = in.readString();
        isSubscribed = (in.readInt() == 1);
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
        parcel.writeInt(id);
        parcel.writeString(name);
        parcel.writeString(description);
        parcel.writeList(bridgeIntervals);
        parcel.writeString(photoBridgeOpenURL);
        parcel.writeString(photoBridgeClosedURL);
        parcel.writeInt(isSubscribed ? 1 : 0);
    }

    public void addInterval(String start, String end) {
        try {
            bridgeIntervals.add(new TimeInterval(start, end));
        } catch (TimeInterval.TimeIntervalInputException e) {
            e.printStackTrace();
        }
    }

    public void putBridgeOpenImage(Context context, ImageView container) {
        Glide.with(context)
                .load(photoBridgeOpenURL)
                .into(container);
    }

    public void putBridgeClosedImage(Context context, ImageView container) {
        Glide.with(context)
                .load(photoBridgeClosedURL)
                .into(container);
    }

    public Bridge(int id, String name, String description, String photoBridgeOpenURL, String photoBridgeClosedURL, boolean isSubscribed, TimeInterval... intervals) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.photoBridgeOpenURL = photoBridgeOpenURL;
        this.photoBridgeClosedURL = photoBridgeClosedURL;
        this.isSubscribed = isSubscribed;
        bridgeIntervals = new ArrayList();
        bridgeIntervals.addAll(Arrays.asList(intervals));
    }
}