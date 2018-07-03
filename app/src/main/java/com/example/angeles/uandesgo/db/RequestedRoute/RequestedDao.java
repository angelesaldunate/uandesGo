package com.example.angeles.uandesgo.db.RequestedRoute;

/**
 * Created by Angeles on 5/30/2018.
 */


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface RequestedDao {

    @Query("SELECT * FROM requestedroute")
    List<RequestedRoute> getAllReqRoute();

    @Query("SELECT routeId FROM requestedroute WHERE userId=:userId")
    List<Integer> getAllIdRoute(int userId);

    @Query("SELECT COUNT(*) FROM requestedroute WHERE routeId=:routeId")
    int getAllRequestsForRoute (int routeId);

    @Query("SELECT * FROM requestedroute WHERE routeId=:routeId AND userId=:userId" )
    RequestedRoute getOneRequestedRoute (int userId,int routeId);


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(RequestedRoute... requestedRoutes);


    @Query("DELETE FROM requestedroute Where rrid=:rRid")
    void delete(int rRid);
}
