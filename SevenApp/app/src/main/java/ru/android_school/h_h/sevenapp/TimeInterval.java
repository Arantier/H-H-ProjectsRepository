package ru.android_school.h_h.sevenapp;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TimeInterval implements Parcelable {
    String start;
    long startMs;
    String end;
    long endMs;

    public static final int BRIDGE_RAISED = 111;
    public static final int BRIDGE_SOON = 110;
    public static final int BRIDGE_CONNECTED = 109;

    protected TimeInterval(Parcel in) {
        start = in.readString();
        startMs = in.readLong();
        end = in.readString();
        endMs = in.readLong();
    }

    public static final Creator<TimeInterval> CREATOR = new Creator<TimeInterval>() {
        @Override
        public TimeInterval createFromParcel(Parcel in) {
            return new TimeInterval(in);
        }

        @Override
        public TimeInterval[] newArray(int size) {
            return new TimeInterval[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(start);
        parcel.writeLong(startMs);
        parcel.writeString(end);
        parcel.writeLong(endMs);
    }

    public static class TimeIntervalInputException extends Exception {
        public TimeIntervalInputException(String message, String start, String end) {
            super(message + "\nStart interval:" + start + "\nEnd interval:" + end);
        }
    }

    //todo: Возможно стоит добавить сразу ввод в строке, а не дате
    public TimeInterval(String start, String end) throws TimeIntervalInputException {
        this.start = start;
        this.end = end;
        Pattern pattern = Pattern.compile("^\\d{1,2}:\\d\\d$");
        if (!pattern.matcher(start).matches() || !pattern.matcher(end).matches()) {
            throw new TimeIntervalInputException("Error:invalid time format", start, end);
        }
        startMs = Integer.parseInt(start.split(":")[0]) * 3600000 + Integer.parseInt(start.split(":")[1]) * 60000;
        endMs = Integer.parseInt(end.split(":")[0]) * 3600000 + Integer.parseInt(end.split(":")[1]) * 60000;
    }

    @Override
    public String toString() {
        String result = start + " - " + end;
        Log.i("intervals","Current intervals:\nstart:"+startMs+" ms;\n"+"end:"+endMs+" ms");
        return result;
    }


    //todo:одоработай
    public int whatPosition(Date time) {
        long now = time.getHours() * 3600000 + time.getMinutes() * 60000;
        Log.i("intervals","Now:"+now+" ms;");
        long diff = startMs - now;
        if (now > startMs) {
            if (now < endMs) {
                if (startMs < endMs) {
                    return BRIDGE_RAISED;
                } else {
                    if ((86400000-diff) <= 3600000) {
                        return BRIDGE_SOON;
                    } else {
                        return BRIDGE_CONNECTED;
                    }
                }
            } else {
                return BRIDGE_RAISED;
            }
        } else {
            if (now<endMs){
                if (startMs<endMs){
                    if (diff<3600000){
                        return BRIDGE_SOON;
                    } else {
                        return BRIDGE_CONNECTED;
                    }
                } else {
                    return BRIDGE_RAISED;
                }
            } else {
                if (diff<3600000){
                    return BRIDGE_SOON;
                } else {
                    return BRIDGE_CONNECTED;
                }
            }
        }
    }
}
