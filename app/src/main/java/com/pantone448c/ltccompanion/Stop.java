package com.pantone448c.ltccompanion;

import com.mapbox.geojson.Feature;

public final class Stop {
    public final int STOP_ID;
    public final int STOP_CODE;
    public final String STOP_NAME;
    public final float STOP_LAT;
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
