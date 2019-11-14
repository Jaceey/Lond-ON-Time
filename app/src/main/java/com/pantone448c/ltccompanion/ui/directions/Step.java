package com.pantone448c.ltccompanion.ui.directions;

public abstract class Step {
    public final String distance;
    public final String duration;
    public final TravelMode travelMode;

    public Step(final String distance, final String duration, final TravelMode travelMode)
    {
        this.distance = distance;
        this.duration = duration;
        this.travelMode = travelMode;
    }
}
