package com.pantone448c.ltccompanion;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "freq_stops")
public final class Stop {
    @PrimaryKey
    @NonNull
    public final int STOP_ID;
    @NonNull
    public final int STOP_CODE;
    @NonNull
    public final String STOP_NAME;
    @NonNull
    public final float STOP_LAT;
    @NonNull
    public final float STOP_LON;
    public final int WHEELCHAIR_BOARDING;

    public Stop (final int STOP_ID, final int STOP_CODE, final String STOP_NAME, final float STOP_LAT, final float STOP_LON, final int WHEELCHAIR_BOARDING)
    {
        this.STOP_ID = STOP_ID;
        this.STOP_CODE = STOP_CODE;
        this.STOP_NAME = STOP_NAME;
        this.STOP_LAT = STOP_LAT;
        this.STOP_LON = STOP_LON;
        this.WHEELCHAIR_BOARDING = WHEELCHAIR_BOARDING;
    }
}
