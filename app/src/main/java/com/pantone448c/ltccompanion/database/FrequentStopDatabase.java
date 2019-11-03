package com.pantone448c.ltccompanion.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.pantone448c.ltccompanion.Stop;

@Database(entities = {Stop.class}, version = 1)
public abstract class FrequentStopDatabase extends RoomDatabase {
    public abstract StopDAO stopDAO();

    private static volatile FrequentStopDatabase INSTANCE;

    static FrequentStopDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (FrequentStopDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            FrequentStopDatabase.class, "freq_stops_database")
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
