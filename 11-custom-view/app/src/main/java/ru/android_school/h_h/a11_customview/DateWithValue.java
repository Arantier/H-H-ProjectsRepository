package ru.android_school.h_h.a11_customview;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Comparator;

public class DateWithValue implements Comparable<DateWithValue>, Comparator<DateWithValue> {
    int value;
    Calendar date;

    public DateWithValue(int value, Calendar date) {
        this.value = value;
        this.date = date;
    }

    @Override
    public int compareTo(@NonNull DateWithValue dateWithValue) {
        return this.date.compareTo(dateWithValue.date);
    }

    public String getFancyDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM");
        return sdf.format(date.getTime());
    }

    @Override
    public int compare(DateWithValue dateWithValue, DateWithValue t1) {
        return dateWithValue.compareTo(t1);
    }
}
