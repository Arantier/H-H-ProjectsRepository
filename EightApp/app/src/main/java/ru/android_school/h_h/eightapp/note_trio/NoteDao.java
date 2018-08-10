package ru.android_school.h_h.eightapp.note_trio;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.reactivex.Single;

@Dao
public interface NoteDao {

    @Query("SELECT * FROM Note")
    Flowable<List<Note>> getAll();

    @Query("SELECT * FROM Note WHERE isArchived = 0")
    Flowable<List<Note>> getAllLive();

    @Query("SELECT * FROM Note WHERE "+
    "title LIKE '%'||:stringToSearch||'%' OR "+
    "text LIKE '%'||:stringToSearch||'%'")
   List<Note> getBySearch(String stringToSearch);

    @Insert
    void insert(Note ...newNote);

    @Delete
    void delete(Note note);

    @Update
    void update(Note note);
}
