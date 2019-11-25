package com.pantone448c.ltccompanion.ui.directions;

public class TransitStep extends Step {

    public final TransitDetails transit_details;
    public TransitStep(final GooglePair distance, final GooglePair duration, final GoogleLocation end_location, final GoogleLocation start_location, final String travel_mode, final String html_instructions, final TransitDetails transit_details)
    {
        super(distance, duration, end_location, start_location, travel_mode, html_instructions);
        this.transit_details = transit_details;
    }
}
