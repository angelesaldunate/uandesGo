package com.example.angeles.uandesgo.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.angeles.uandesgo.db.Place.Place;
import com.example.angeles.uandesgo.db.Place.PlaceDao;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedDao;
import com.example.angeles.uandesgo.db.RequestedRoute.RequestedRoute;
import com.example.angeles.uandesgo.db.Route.Route;
import com.example.angeles.uandesgo.db.Route.RouteDao;
import com.example.angeles.uandesgo.db.User.Profile.Profile;
import com.example.angeles.uandesgo.db.User.Profile.ProfileDao;
import com.example.angeles.uandesgo.db.User.User;
import com.example.angeles.uandesgo.db.User.UserDao;

/**
 * Created by Angeles on 5/30/2018.
 */


@Database(entities = {User.class, Place.class, Route.class, RequestedRoute.class, Profile.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
    public abstract PlaceDao placeDao();
    public abstract RouteDao routeDao();
    public abstract RequestedDao requestedDao();
    public abstract ProfileDao profileDao();


    private static AppDatabase INSTANCE;

    public static AppDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (AppDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            AppDatabase.class, "app_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
