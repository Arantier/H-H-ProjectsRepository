package ru.android_school.h_h.eightapp.note_trio;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import io.reactivex.annotations.NonNull;
import ru.android_school.h_h.eightapp.R;

@Entity
public class Note implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String title;
    public String text;
    public int backgroundColorResource;
    public boolean isArchived;

    @Ignore
    public Note(String title, String text) {
        this.title = title;
        this.text = text;
        backgroundColorResource = R.color.white;
        isArchived = false;
    }

    public Note(String title, String text, int backgroundColorResource) {
        this(title, text);
        this.backgroundColorResource = backgroundColorResource;
    }

    @Ignore
    protected Note(Parcel in) {
        id = in.readInt();
        title = in.readString();
        text = in.readString();
        backgroundColorResource = in.readInt();
        isArchived = in.readByte() != 0;
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

    @Override
    public String toString() {
        return "Current note\n"+
                "Title: "+title+"\n"+
                "Text: "+text+"\n"+
                "Background: "+backgroundColorResource+"\n"+
                "Archive status: "+((isArchived) ? "Archived" : "None");
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(title);
        parcel.writeString(text);
        parcel.writeInt(backgroundColorResource);
        parcel.writeByte((byte) ((isArchived) ? 1 : 0));
    }
}
