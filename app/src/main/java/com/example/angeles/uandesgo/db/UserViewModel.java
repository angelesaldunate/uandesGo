package com.example.angeles.uandesgo.db;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by Angeles on 5/30/2018.
 */

public class UserViewModel extends AndroidViewModel {

    private UserRepository mRepository;

    private LiveData<List<User>> mAllUsers;

    public UserViewModel(@NonNull Application application) {
        super(application);
        mRepository = new UserRepository(application);
        mAllUsers = mRepository.getAllUsers();
    }

    public LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public void insert(List<User> users) {
        mRepository.insert(users);
    }
}
