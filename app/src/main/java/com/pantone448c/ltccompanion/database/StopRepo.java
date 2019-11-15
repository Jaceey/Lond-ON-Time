package com.pantone448c.ltccompanion.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.pantone448c.ltccompanion.Stop;


import java.util.List;

public class StopRepo {
    private StopDAO mStopDao;
    private LiveData<List<Stop>> mFreqStops;

    private MutableLiveData<List<Stop>> searchResults = new MutableLiveData<>();
    private void asyncQueryFinished(List<Stop> results){
        searchResults.setValue(results);
    }
    public Stop getFirstResult(){
        List<Stop> result = (List<Stop>) searchResults.getValue();
        return result.get(0);
    }
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

    public void getStopByID(Stop stop){
        //new insertAsyncTask(mStopDao).execute(stop);
        //return null;
        queryAsyncTask task = new queryAsyncTask(mStopDao, this);
        task.execute(stop.STOP_ID);
    }

    public void insertStop(Stop stop)
    {
        new insertAsyncTask(mStopDao).execute(stop);
    }

    public void deleteStop(Stop stop)
    {
        new deleteAsyncTask(mStopDao).execute(stop);
    }

    private static class queryAsyncTask extends AsyncTask<Integer, Void, List<Stop>>
    {
        private StopDAO mAsyncTaskDao;
        private static StopRepo stopRepo;

        queryAsyncTask(StopDAO dao, StopRepo repo){ mAsyncTaskDao = dao; stopRepo = repo; }

        @Override
        protected List<Stop> doInBackground(final Integer... params){
            return mAsyncTaskDao.getStopByID(params[0]);
        }

        @Override
        protected void onPostExecute(List<Stop> result){
            stopRepo.asyncQueryFinished(result);
        }
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
