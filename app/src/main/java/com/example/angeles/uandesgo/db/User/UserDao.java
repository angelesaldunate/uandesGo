package com.example.angeles.uandesgo.db;

/**
 * Created by Angeles on 5/30/2018.
 */


import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface UserDao {

    @Query("SELECT * FROM user")
    List<User> getAllUser();
    @Query("SELECT * FROM user WHERE email=:email LIMIT 1")
    User getOneUser(String email);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(User... user);


    @Query("DELETE FROM user")
    void deleteAll();

    @Query("SELECT * FROM user WHERE uid=:user_id")
    User getUserById(int user_id);
}
