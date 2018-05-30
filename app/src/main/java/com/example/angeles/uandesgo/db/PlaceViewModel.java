package com.example.angeles.uandesgo.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Angeles on 5/30/2018.
 */

public class PlaceViewModel extends AndroidViewModel {

    private PlaceRepository mRepository;

    private LiveData<List<Place>> mAllPlaces;

    public PlaceViewModel(@NonNull Application application) {
        super(application);
        mRepository = new PlaceRepository(application);
        mAllPlaces = mRepository.getAllPlaces();
    }

    public LiveData<List<Place>> getmAllPlaces() {
        return mAllPlaces;
    }

    public void insert(List<Place> places) {
        mRepository.insert(places);
    }
}
