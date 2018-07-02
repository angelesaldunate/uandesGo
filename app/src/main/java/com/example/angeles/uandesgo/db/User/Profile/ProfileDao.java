package com.example.angeles.uandesgo.db.User.Profile;

/**
 * Created by Angeles on 5/30/2018.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.angeles.uandesgo.db.User.User;

import java.util.List;

@Dao
public interface ProfileDao {

    @Query("SELECT * FROM profile")
    List<Profile> getAllProfile();
    @Query("SELECT * FROM profile WHERE userId=:userId LIMIT 1")
    Profile getOneProfile(int userId);

    @Insert
    void insertAll(Profile... profile);


    @Query("DELETE FROM profile")
    void deleteAll();

    @Update
    void update(Profile profile);

    @Query("SELECT * FROM profile WHERE prid=:profile_id")
    Profile getProfileById(int profile_id);
}
