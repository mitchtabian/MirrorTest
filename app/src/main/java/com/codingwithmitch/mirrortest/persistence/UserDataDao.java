package com.codingwithmitch.mirrortest.persistence;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;


@Dao
public interface UserDataDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUsers(UserDataEntity... users);

    @Update
    public void updateUsers(UserDataEntity... users);

    @Delete
    public void deleteUsers(UserDataEntity... users);

    @Query("SELECT * FROM UserDataEntity WHERE email = :userEmail")
    public UserDataEntity getUser(String userEmail);

}
