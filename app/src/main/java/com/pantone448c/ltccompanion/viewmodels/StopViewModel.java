package com.pantone448c.ltccompanion.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pantone448c.ltccompanion.Stop;
import com.pantone448c.ltccompanion.database.StopRepo;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class StopViewModel extends AndroidViewModel {
    private StopRepo stopRepo;
    private LiveData<List<Stop>> stops;

    public StopViewModel (Application application)
    {
        super(application);
        stopRepo = new StopRepo(application);
        stops = stopRepo.getFrequentStops();
    }

    public LiveData<List<Stop>> getStops()
    {
        return stops;
    }

    public Stop getStopById(int stopID) throws ExecutionException, InterruptedException {
        return stopRepo.getStopByID(stopID);
    }

    public void insertStop(Stop stop)
    {
        stopRepo.insertStop(stop);
    }

    public void deleteStop(Stop stop)
    {
        stopRepo.deleteStop(stop);
    }

}
