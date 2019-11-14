package com.pantone448c.ltccompanion.ui.directions;

public class TransitStep extends Step {
    public final String arrival_stop;
    public final String arrival_time;
    public final String departure_stop;
    public final String departure_time;
    public final String name;
    public final String headsign;
    public final int numStops;

    public TransitStep(final String distance, final String duration, final String arrival_stop, final String arrival_time, final String departure_stop, final String departure_time, final String name, final String headsign, final int numStops)
    {
        super(distance, duration, TravelMode.TRANSIT);
        this.arrival_stop = arrival_stop;
        this.arrival_time = arrival_time;
        this.departure_stop = departure_stop;
        this.departure_time = departure_time;
        this.name = name;
        this.headsign = headsign;
        this.numStops = numStops;
    }
}
