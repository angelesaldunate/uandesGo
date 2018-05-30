package com.example.angeles.uandesgo.db;

/**
 * Created by Angeles on 5/30/2018.
 */

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


public class PlaceRepository {

    private PlaceDao mPlaceDao;
    private LiveData<List<Place>> mAllPlaces;

    PlaceRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mPlaceDao = db.placeDao();
        mAllPlaces = mPlaceDao.getAllPlaces();
    }

    LiveData<List<Place>> getAllPlaces() {
        return mAllPlaces;
    }

    public void insert (List<Place> places) {
        Place[] placesArray = new Place[places.size()];
        placesArray = places.toArray(placesArray);
        new insertAsyncTask(mPlaceDao).execute(placesArray);
    }

    private static class insertAsyncTask extends AsyncTask<Place, Void, Void> {

        private PlaceDao mAsyncTaskDao;

        insertAsyncTask(PlaceDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Place... params) {
            mAsyncTaskDao.insertAll(params);
            return null;
        }
    }
}
