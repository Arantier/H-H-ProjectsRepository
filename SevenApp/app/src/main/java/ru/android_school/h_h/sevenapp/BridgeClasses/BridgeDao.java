package ru.android_school.h_h.sevenapp.BridgeClasses;

import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.ArrayList;

import io.reactivex.Single;

public interface BridgeDao {

    @Query("SELECT * FROM Bridge")
    Single<ArrayList<Bridge>> getAll();

    @Query("SELECT * FROM Bridge WHERE id LIKE (:id)")
    Single<Bridge> get(int id);

    @Insert
    void insertList(ArrayList<Bridge> list);
}
