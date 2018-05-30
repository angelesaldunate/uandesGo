package com.example.angeles.uandesgo.db;

/**
 * Created by Angeles on 5/30/2018.
 */

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;


public class UserRepository {

    private UserDao mUserDao;
    private LiveData<List<User>> mAllUsers;

    UserRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        mUserDao = db.userDao();
        mAllUsers = mUserDao.getAllUser();
    }

    LiveData<List<User>> getAllUsers() {
        return mAllUsers;
    }

    public void insert (List<User> users) {
        User[] usersArray = new User[users.size()];
        usersArray = users.toArray(usersArray);
        new insertAsyncTask(mUserDao).execute(usersArray);
    }

    private static class insertAsyncTask extends AsyncTask<User, Void, Void> {

        private UserDao mAsyncTaskDao;

        insertAsyncTask(UserDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final User... params) {
            mAsyncTaskDao.insertAll(params);
            return null;
        }
    }
}
