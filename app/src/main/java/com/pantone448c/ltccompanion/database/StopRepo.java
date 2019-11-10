package com.pantone448c.ltccompanion.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.pantone448c.ltccompanion.Stop;


import java.util.List;

public class StopRepo {
    private StopDAO mStopDao;
    private LiveData<List<Stop>> mFreqStops;

    public StopRepo(Application application)
    {
        FrequentStopDatabase db = FrequentStopDatabase.getDatabase(application);
        mStopDao = db.stopDAO();
        mFreqStops = mStopDao.getStops();
    }

    public LiveData<List<Stop>> getFrequentStops()
    {
        return mFreqStops;
    }

    public void insertStop(Stop stop)
    {
        new insertAsyncTask(mStopDao).execute(stop);
    }

    public void deleteStop(Stop stop)
    {
        new deleteAsyncTask(mStopDao).execute(stop);
    }

    private static class insertAsyncTask extends AsyncTask<Stop, Void, Void>
    {
        private StopDAO mAsyncTaskDao;

        insertAsyncTask(StopDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stop... params)
        {
            mAsyncTaskDao.insertStop(params[0]);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<Stop, Void, Void>
    {
        private StopDAO mAsyncTaskDao;

        deleteAsyncTask(StopDAO dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Stop... params)
        {
            mAsyncTaskDao.deleteStop(params[0]);
            return null;
        }
    }


}
