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
public interface PlaceDao {

    @Query("SELECT * FROM place")
    List<Place> getAllPlaces();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Place... place);


    @Query("DELETE FROM place")
    void deleteAll();
}
