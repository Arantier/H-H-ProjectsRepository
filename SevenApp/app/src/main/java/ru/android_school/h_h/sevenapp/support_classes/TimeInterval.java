package ru.android_school.h_h.sevenapp.support_classes;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;
import java.util.regex.Pattern;

public class TimeInterval implements Parcelable {
    String start;
    String end;
    Calendar startCalendar;
    Calendar endCalendar;

    public static final String TAG = "TimeInterval";

    public static final int INSIDE_INTERVAL = 2;
    public static final int LESSER_THAN_HOUR = 1;
    public static final int MORE_THAN_HOUR = 0;

    protected TimeInterval(Parcel in) {
        start = in.readString();
        end = in.readString();
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.setTimeInMillis(in.readLong());
        endCalendar.setTimeInMillis(in.readLong());
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
        parcel.writeString(end);
        parcel.writeLong(startCalendar.getTimeInMillis());
        parcel.writeLong(endCalendar.getTimeInMillis());
    }

    public static class TimeIntervalInputException extends Exception {
        public TimeIntervalInputException(String message, String start, String end) {
            super(message + "\nStart interval:" + start + "\nEnd interval:" + end);
        }
    }

    public int currentState() {
        update();
        Calendar now = Calendar.getInstance();
        Calendar diff = ((Calendar) startCalendar.clone());
        //Интервал всегда такой, что он или до, или после, или во время нас
        Log.i("currentStateTag", "Начальная точка:" + startCalendar.get(Calendar.DAY_OF_YEAR) + " " + startCalendar.get(Calendar.HOUR_OF_DAY) + ":" + startCalendar.get(Calendar.MINUTE) + "\n"
                + "Конечная точка:" + endCalendar.get(Calendar.DAY_OF_YEAR) + " " + endCalendar.get(Calendar.HOUR_OF_DAY) + ":" + endCalendar.get(Calendar.MINUTE)
                + "Нынешняя точка:" + now.get(Calendar.DAY_OF_YEAR) + " " + now.get(Calendar.HOUR_OF_DAY) + ":" + now.get(Calendar.MINUTE));
        int returnValue = 0;
        if (now.compareTo(startCalendar) < 0) {
            diff.add(Calendar.MINUTE, -60);
            Log.i("currentStateDiffTag","Время на час меньше старта:"+diff.get(Calendar.HOUR_OF_DAY)+":"+diff.get(Calendar.MINUTE)+"\t"+diff.get(Calendar.DATE)+" day");
            if (diff.compareTo(now)<=0) {
                returnValue = LESSER_THAN_HOUR;
            } else {
                returnValue = MORE_THAN_HOUR;
            }
        } else {
            returnValue = INSIDE_INTERVAL;
        }
        Log.i("currentStateDiffTag","Выбранное значение:"+returnValue);
        return returnValue;
    }

    public TimeInterval(String start, String end) throws TimeIntervalInputException {
        this.start = start;
        this.end = end;
        Pattern pattern = Pattern.compile("^\\d{1,2}:\\d\\d$");
        if (!pattern.matcher(start).matches() || !pattern.matcher(end).matches()) {
            throw new TimeIntervalInputException("Error:invalid time format", start, end);
        }
        int startHour = Integer.parseInt(start.split(":")[0]);
        int startMinute = Integer.parseInt(start.split(":")[1]);
        int endHour = Integer.parseInt(end.split(":")[0]);
        int endMinute = Integer.parseInt(end.split(":")[1]);
        startCalendar = Calendar.getInstance();
        endCalendar = Calendar.getInstance();
        startCalendar.set(Calendar.HOUR_OF_DAY, startHour);
        startCalendar.set(Calendar.MINUTE, startMinute);
        endCalendar.set(Calendar.HOUR_OF_DAY, endHour);
        endCalendar.set(Calendar.MINUTE, endMinute);
        update();
    }

    public void update() {
        if (startCalendar.compareTo(endCalendar)<0){
            if (endCalendar.compareTo(Calendar.getInstance())<0){
                startCalendar.add(Calendar.DATE,1);
                endCalendar.add(Calendar.DATE,1);
            }
        } else {
            if (endCalendar.compareTo(Calendar.getInstance())>0){
                startCalendar.add(Calendar.DATE,-1);
            } else {
                endCalendar.add(Calendar.DATE,1);
            }
        }
    }

    @Override
    public String toString() {
        String result = start + " - " + end;
        Log.i(TAG, "Current intervals:\nstart:" + startCalendar + " ms;\n" + "end:" + endCalendar + " ms");
        return result;
    }

}
