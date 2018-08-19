package com.example.dmitry.testingnotifications;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcelableClass implements Parcelable {

    String title;
    String text;

    public ParcelableClass(String title, String text) {
        this.title = title;
        this.text = text;
    }

    protected ParcelableClass(Parcel in) {
        title = in.readString();
        text = in.readString();
    }

    public static final Creator<ParcelableClass> CREATOR = new Creator<ParcelableClass>() {
        @Override
        public ParcelableClass createFromParcel(Parcel in) {
            return new ParcelableClass(in);
        }

        @Override
        public ParcelableClass[] newArray(int size) {
            return new ParcelableClass[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(title);
        parcel.writeString(text);
    }
}
