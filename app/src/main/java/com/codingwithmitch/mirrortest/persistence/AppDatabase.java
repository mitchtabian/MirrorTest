package com.codingwithmitch.mirrortest.persistence;

import android.arch.persistence.db.SupportSQLiteOpenHelper;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.DatabaseConfiguration;
import android.arch.persistence.room.InvalidationTracker;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.support.annotation.NonNull;

@Database(entities = {UserDataEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static AppDatabase mInstance;
    public abstract UserDataDao userDataDao();

    public static AppDatabase getInMemoryDatabase(Context context) {
        if (mInstance == null) {
            mInstance =
                    Room.inMemoryDatabaseBuilder(context.getApplicationContext(), AppDatabase.class)
                            .allowMainThreadQueries()
                            .build();
        }
        return mInstance;
    }

    public static void destroyInstance() {
        mInstance = null;
    }
}
