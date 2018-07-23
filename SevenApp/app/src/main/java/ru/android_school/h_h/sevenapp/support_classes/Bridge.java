package ru.android_school.h_h.sevenapp.support_classes;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

public class Bridge implements Parcelable {
    private int id;
    private String name;
    private String nameEng;
    private String description;
    private String descriptionEng;
    public ArrayList<TimeInterval> bridgeIntervals;
    private String photoBridgeOpenURL;
    private String photoBridgeClosedURL;
    public int timeToRemindInMinutes;
    private double latitude;
    private double longtitude;

    public static final String TAG = "bridge";

    public static final int BRIDGE_RAISED = 2;
    public static final int BRIDGE_SOON = 1;
    public static final int BRIDGE_CONNECTED = 0;
    public static final int NO_REMIND = -1;

    public Bridge() {
        bridgeIntervals = new ArrayList<>();
        timeToRemindInMinutes = NO_REMIND;
    }

    protected Bridge(Parcel in) {
        setId(in.readInt());
        setName(in.readString());
        setDescription(in.readString());
        bridgeIntervals = in.readArrayList(TimeInterval.class.getClassLoader());
        setPhotoBridgeOpenURL(in.readString());
        setPhotoBridgeClosedURL(in.readString());
        timeToRemindInMinutes = in.readInt();
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
        parcel.writeList(bridgeIntervals);
        parcel.writeString(getPhotoBridgeOpenURL());
        parcel.writeString(getPhotoBridgeClosedURL());
        parcel.writeInt(timeToRemindInMinutes);
    }

    public void addInterval(String start, String end) {
        try {
            bridgeIntervals.add(new TimeInterval(start, end));
        } catch (TimeInterval.TimeIntervalInputException e) {
            Log.e(TAG,"Wrong interval:"+e);
        }
    }

    public int currentBridgeState(){
        int bridgeState = BRIDGE_CONNECTED;
        for (TimeInterval interval : bridgeIntervals){
            int intervalState = interval.currentState();
            int possibleBridgeState;
            switch (intervalState){
                case (TimeInterval.MORE_THAN_HOUR) :
                    possibleBridgeState = BRIDGE_CONNECTED;
                    break;
                case (TimeInterval.LESSER_THAN_HOUR) :
                    possibleBridgeState = BRIDGE_SOON;
                    break;
                default:
                    possibleBridgeState = BRIDGE_RAISED;
                    break;
            }
            if (possibleBridgeState>bridgeState) {
                bridgeState = possibleBridgeState;
            }
        }
        return bridgeState;
    }

    public Calendar getClosestStart(){
        Calendar closestStart = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
        closestStart.add(Calendar.DATE,2);
        for (TimeInterval interval : bridgeIntervals){
            if ((closestStart.compareTo(interval.startCalendar)>0)&&(closestStart.compareTo(now)>0)){
                closestStart = ((Calendar) interval.startCalendar.clone());
            }
        }
        Log.i(TAG,"Closest start is:"+closestStart);
        return closestStart;
    }

    public void putBridgeOpenImage(Context context, ImageView container) {
        Glide.with(context)
                .load(getPhotoBridgeOpenURL())
                .into(container);
    }

    public void putBridgeClosedImage(Context context, ImageView container) {
        Glide.with(context)
                .load(getPhotoBridgeClosedURL())
                .into(container);
    }

    public Bridge(int id, String name, String description, String photoBridgeOpenURL, String photoBridgeClosedURL, int timeToRemindInMinutes, TimeInterval... intervals) {
        this.setId(id);
        this.setName(name);
        this.setDescription(description);
        this.setPhotoBridgeOpenURL(photoBridgeOpenURL);
        this.setPhotoBridgeClosedURL(photoBridgeClosedURL);
        this.timeToRemindInMinutes = timeToRemindInMinutes;
        bridgeIntervals = new ArrayList();
        bridgeIntervals.addAll(Arrays.asList(intervals));
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNameEng() {
        return nameEng;
    }

    public void setNameEng(String nameEng) {
        this.nameEng = nameEng;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescriptionEng() {
        return descriptionEng;
    }

    public void setDescriptionEng(String descriptionEng) {
        this.descriptionEng = descriptionEng;
    }

    public String getPhotoBridgeOpenURL() {
        return photoBridgeOpenURL;
    }

    public void setPhotoBridgeOpenURL(String photoBridgeOpenURL) {
        this.photoBridgeOpenURL = photoBridgeOpenURL;
    }

    public String getPhotoBridgeClosedURL() {
        return photoBridgeClosedURL;
    }

    public void setPhotoBridgeClosedURL(String photoBridgeClosedURL) {
        this.photoBridgeClosedURL = photoBridgeClosedURL;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    @Override
    public String toString(){
        String result = "";
        result+="id:"+id+"\n";
        result+="description:"+description+"\n";
        for (TimeInterval i : bridgeIntervals){
            result+="\tInterval:"+i+"\n";
        }
        return result;
    }

    public static Bridge makeDebugBridge(){
        try {
            Bridge debug = new Bridge(0,"Отладочный мост","Описание","http://it-increment.ru/wp-content/uploads/2016/09/ujniy-most.jpg","http://it-increment.ru/wp-content/uploads/2016/09/ujniy-most.jpg",NO_REMIND,new TimeInterval("0:00","23:59"));
            return debug;
        } catch (TimeInterval.TimeIntervalInputException e) {
            e.printStackTrace();
            return null;
        }
    }
}