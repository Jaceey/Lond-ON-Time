package com.pantone448c.ltccompanion;

public class StopTime {
    public final int TRIP_ID;
    public final String ARRIVAL_TIME;
    public final String DEPARTURE_TIME;
    public final int STOP_ID;
    public final int STOP_SEQUENCE;

    public StopTime(final int TRIP_ID, final String ARRIVAL_TIME, final String DEPARTURE_TIME, final int STOP_ID, final int STOP_SEQUENCE)
    {
        this.TRIP_ID = TRIP_ID;
        this.ARRIVAL_TIME = ARRIVAL_TIME;
        this.DEPARTURE_TIME = DEPARTURE_TIME;
        this.STOP_ID = STOP_ID;
        this.STOP_SEQUENCE = STOP_SEQUENCE;
    }
}
