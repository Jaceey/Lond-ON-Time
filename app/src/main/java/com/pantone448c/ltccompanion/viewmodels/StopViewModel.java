package com.pantone448c.ltccompanion.viewmodels;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.pantone448c.ltccompanion.Stop;
import com.pantone448c.ltccompanion.database.FrequentStopDatabase;
import com.pantone448c.ltccompanion.database.StopDAO;

import java.util.List;

public class StopViewModel extends AndroidViewModel {
    private StopDAO dao;
    private LiveData<List<Stop>> stops;
    public StopViewModel (Application application)
    {
        super(application);
        dao = FrequentStopDatabase.getDatabase(application).stopDAO();
    }

    public LiveData<List<Stop>> getStops()
    {
        return stops;
    }

    public void insert(Stop stop)
    {
        dao.insertStop(stop);
    }
}
