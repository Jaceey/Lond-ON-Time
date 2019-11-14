package com.pantone448c.ltccompanion.ui.directions;

public class TransitTrip {

    public final String arrival_time;
    public final String departure_time;
    public final String distance;
    public final String duration;
    public final String startAddress;
    public final String endAddress;
    public final Step[] steps;

    public TransitTrip(String arrival_time, String departure_time, String distance, String duration, String startAddress, String endAddress, Step[] steps)
    {
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.distance = distance;
        this.duration = duration;
        this.startAddress = startAddress;
        this.endAddress = endAddress;
        this.steps = steps;
    }
}
