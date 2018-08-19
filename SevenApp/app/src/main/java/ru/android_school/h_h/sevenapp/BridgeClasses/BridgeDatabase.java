package ru.android_school.h_h.sevenapp.BridgeClasses;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Bridge.class}, version = 1)
public abstract class BridgeDatabase extends RoomDatabase {
    public abstract BridgeDao bridgeDao();
}
