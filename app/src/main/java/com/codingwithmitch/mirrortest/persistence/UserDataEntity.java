package com.codingwithmitch.mirrortest.persistence;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(indices = {@Index(value = {"email"}, unique = true)})
public class UserDataEntity {

    @PrimaryKey
    public int id;

    public String email;
    public String name;
    public String birthdate;
    public String location;
    public Long timestamp;

}
