package com.pantone448c.ltccompanion.ui.directions;

public class Leg {
    public final GoogleTime arrival_time;
    public final GoogleTime departure_time;
    public final GooglePair distance;
    public final GooglePair duration;
    public final String end_address;
    public final GoogleLocation end_location;
    public final String start_address;
    public final GoogleLocation start_location;
    public final Step[] steps;

    public Leg(final GoogleTime arrival_time, final GoogleTime departure_time, final GooglePair distance, final GooglePair duration, final String end_address, final GoogleLocation end_location, final String start_address, final GoogleLocation start_location, final Step[] steps)
    {
        this.arrival_time = arrival_time;
        this.departure_time = departure_time;
        this.distance = distance;
        this.duration = duration;
        this.end_address = end_address;
        this.end_location = end_location;
        this.start_address = start_address;
        this.start_location = start_location;
        this.steps = steps;
    }
}
