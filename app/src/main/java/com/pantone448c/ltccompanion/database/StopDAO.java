package com.pantone448c.ltccompanion.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.pantone448c.ltccompanion.Stop;

import java.util.List;

@Dao
public interface StopDAO {

    @Query("select * from freq_stops")
    public LiveData<List<Stop>> getStops();

    @Query("select * from freq_stops where stop_id=:stopid")
    public Stop getStopByID(int stopid);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertStop(Stop stop);

    @Delete
    public void deleteStop(Stop stop);
}
