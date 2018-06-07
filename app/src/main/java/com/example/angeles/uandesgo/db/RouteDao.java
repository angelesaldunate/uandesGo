package com.example.angeles.uandesgo.db;

/**
 * Created by Angeles on 5/30/2018.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RouteDao {

    @Query("SELECT * FROM route")
    List<Route> getAllRoutes();

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Route... routes);


    @Query("DELETE FROM route")
    void deleteAll();
}