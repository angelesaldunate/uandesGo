package com.example.angeles.uandesgo.db.Place;

/**
 * Created by Angeles on 5/30/2018.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PlaceDao {

    @Query("SELECT * FROM place")
    List<Place> getAllPlaces();

    @Query("SELECT * FROM place WHERE name=:name LIMIT 1")
    Place getOnePlace(String name);

    @Query("SELECT * FROM place WHERE pid=:placeId LIMIT 1")
    Place getPlacebyId(int placeId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Place... place);

    @Query("SELECT zone FROM place GROUP BY zone")
    List<String> getNamesZones();

    @Query("Select pid FROM place WHERE zone=:zone")
    List<Integer> getIndexByPlace(String zone);

    @Query("DELETE FROM place")
    void deleteAll();
}
