package com.pantone448c.ltccompanion.ui.directions;

public class TransitDetails {
    public final GoogleStop arrival_stop;
    public final GoogleTime arrival_time;
    public final GoogleStop departure_stop;
    public final GoogleTime departure_time;
    public final String headsign;
    public final int num_stops;

    public TransitDetails(final GoogleStop arrival_stop, final GoogleTime arrival_time, final GoogleStop departure_stop, final GoogleTime departure_time, final String headsign, final int num_stops)
    {
        this.arrival_stop = arrival_stop;
        this.arrival_time = arrival_time;
        this.departure_stop = departure_stop;
        this.departure_time = departure_time;
        this.headsign = headsign;
        this.num_stops = num_stops;
    }
}
