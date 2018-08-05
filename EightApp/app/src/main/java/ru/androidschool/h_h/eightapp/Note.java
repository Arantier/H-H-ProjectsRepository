package ru.androidschool.h_h.eightapp;

import android.os.Parcel;
import android.os.Parcelable;

public class Note implements Parcelable{

    public String title;
    public String text;
    public int backgroundColor;
    private boolean archiveStatus;

    public Note() {
        title = "";
        text = "";
        backgroundColor = 0xFFFFFF;
        archiveStatus = false;
    }

    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        backgroundColor = 0xFFFFFF;
        archiveStatus = false;
    }

    public Note(String title, String text, int backgroundColor) {
        this(title, text);
        this.backgroundColor = backgroundColor;
    }

    protected Note(Parcel in) {
        title = in.readString();
        text = in.readString();
        backgroundColor = in.readInt();
        archiveStatus = in.readByte() != 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public void makeArchive(boolean state) {
        archiveStatus = state;
    }

    public boolean isArchived() {
        return archiveStatus;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setColor(int color) {
        backgroundColor = color;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeInt(backgroundColor);
        parcel.writeByte((byte) (archiveStatus ? 1 : 0));
    }

    @Override
    public String toString() {
        String result = "Title:"+title+"\nText:"+text+"\nBackground color:"+backgroundColor+"\nIs archived:"+(archiveStatus ? "Yes" : "No");
        return result;
    }
}
