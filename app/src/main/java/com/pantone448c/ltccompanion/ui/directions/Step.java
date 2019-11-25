package com.pantone448c.ltccompanion.ui.directions;

public abstract class Step {
    public final GooglePair distance;
    public final GooglePair duration;
    public final GoogleLocation end_location;
    public final GoogleLocation start_location;
    public final String travel_mode;
    public final String html_instructions;

    public Step(final GooglePair distance, final GooglePair duration, final GoogleLocation end_location, final GoogleLocation start_location, final String travel_mode, final String html_instructions)
    {
        this.distance = distance;
        this.duration = duration;
        this.end_location = end_location;
        this.start_location = start_location;
        this.travel_mode = travel_mode;
        this.html_instructions = html_instructions;
    }
}
